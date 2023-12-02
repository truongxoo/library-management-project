package study.demo.service;

import study.demo.service.dto.response.MessageResponseDto;

public interface BorrowerRecordService {
    
    MessageResponseDto createNewBorrowerRecord(Integer bookId);
    
    MessageResponseDto returnBook(Integer recordId);
    
    
    

}
