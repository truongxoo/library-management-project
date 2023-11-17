package study.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;
import study.demo.enums.Constants;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.DataInvalidException;

@Service
@RequiredArgsConstructor
public class FileUploadingService {
    private final MessageSource messageSource;

    public MessageResponseDto fileUploading(MultipartFile file) {

        Path path = Paths.get(Constants.LOCATION_FILE);
        String fileName = file.getOriginalFilename();
        // checking file type
        if (fileName.endsWith("." + Constants.FILE_TYPE)) {
            // removing the suffix .csv
            fileName = fileName.substring(0, fileName.indexOf("."));
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                return MessageResponseDto.builder()
                        .message(messageSource.getMessage("upload.success",new Object[] {fileName,Constants.LOCATION_FILE.toString()}, Locale.getDefault()))
                        .messageCode("upload.success")
                        .statusCode(HttpStatus.CREATED.value())
                        .build();
            } catch (IOException e) {
                throw new DataInvalidException(messageSource.getMessage("upload.error", null,Locale.getDefault()), "upload.error");
            }
        } else {
            throw new DataInvalidException(
                    messageSource.getMessage("filetype.invalid", null,Locale.getDefault()), "filetype.invalid");
        }
    }

}
