package study.demo.service.dto.response;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.entity.Book;
import study.demo.entity.User;
import study.demo.enums.ERecordStatus;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerRecordDto {
    
    private Integer borrowerRecordId;

    private Instant issueOn;

    private Instant dueDate;

    private String recordStatus;

    private String userName;
    
    private String bookImage;
    
    private String bookTitle;
    
    private String author;
}
