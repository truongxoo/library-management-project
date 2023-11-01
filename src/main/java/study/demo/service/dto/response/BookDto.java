package study.demo.service.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.entity.Author;
import study.demo.entity.Category;
import study.demo.enums.EBookStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Integer bookID;

    private String title;

    private String edition;

    private BigDecimal price;

    private Category category;

    private Author author;

    private String image;

    private EBookStatus status;

    private Integer quantity;
}
