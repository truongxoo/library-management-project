package study.demo.service.dto.request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import study.demo.enums.EBookStatus;

@Data
@Builder
public class BookFilter {

    private Integer bookId;

    private String title;

    private String edition;

    private BigDecimal price;

    private Integer quantity;

    private EBookStatus bookStatus;

}
