package study.demo.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Book;
import study.demo.entity.BorrowerRecord;
import study.demo.entity.User;
import study.demo.enums.Constants;
import study.demo.enums.ERecordStatus;
import study.demo.repository.BookRepository;
import study.demo.repository.BorrowerRecordRepository;
import study.demo.repository.UserRepository;
import study.demo.service.BorrowerRecordService;
import study.demo.service.FileUploadingService;
import study.demo.service.dto.response.MailContentDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.utils.MailSenderUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BorrowerRecordServiceImpl implements BorrowerRecordService {

    private final UserRepository userRepository;

    private final MessageSource messages;

    private final BookRepository bookRepository;

    private final BorrowerRecordRepository recordRepository;
    
    private final FileUploadingService fileUploadingService;
    
    private final MailSenderUtil mailSenderUtil;

    @Override
    public MessageResponseDto createNewBorrowerRecord(Integer bookId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new CusNotFoundException(
                        messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                        "user.notfound"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CusNotFoundException(
                messages.getMessage("book.notfound", null, Locale.getDefault()), "book.notfound"));

        Optional<BorrowerRecord> record = recordRepository.findByUserAndBook(user, book);
        if (record.isPresent()) {
            log.error("Can not borrow two of the same book");
            throw new DataInvalidException(messages.getMessage("book.isborrowed", null, Locale.getDefault()),
                    "book.isborrowed");
        }

        recordRepository
                .save(BorrowerRecord.builder().issueOn(Instant.now()).dueDate(Instant.now().plus(20, ChronoUnit.DAYS))
                        .recordStatus(ERecordStatus.ISSUED).book(book).user(user).build());
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);
        
        
        String content = "<p>Hello " + userName + "</p>" 
        + "<p>Thank you for using our service.</p>"
        + "<p>Make sure you will return book on time</p>"
        +" Check out the attachment below <img src='cid:"+book.getImage()+"'>"
        + "<p>Thank you</p>";
        
        Resource resource = fileUploadingService.previewImage(book.getImage());
        
        MailContentDto mail = MailContentDto.builder()
                .subject("Reset Password")
                .receiver(userName)
                .content(content)
                .resource(resource)
                .build();
        mailSenderUtil.sendMail(mail);

        return MessageResponseDto.builder().messageCode("borrow.book.successfully")
                .message(messages.getMessage("borrow.book.successfully", null, Locale.getDefault())).build();
    }

    // return book and update book quantity
    @Override
    public MessageResponseDto returnBook(Integer recordId) {
        
        BorrowerRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new CusNotFoundException(
                        messages.getMessage("borrowerrecord.notfound", null, Locale.getDefault()),
                        "borrowerrecord.notfound"));

        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        record.setRecordStatus(ERecordStatus.RETURNED);
        recordRepository.save(record);

        return MessageResponseDto.builder().messageCode("return.book.successfully")
                .message(messages.getMessage("return.book.successfully", null, Locale.getDefault())).build();
    }
}
