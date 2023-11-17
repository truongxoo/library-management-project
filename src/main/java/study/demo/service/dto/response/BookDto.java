package study.demo.service.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    
    private Integer bookID;
    
    private String title;
    
    private String edition;

    private BigDecimal price;
    
    private String category;
    
    private String author;
    
    private String image;
    
    private Integer quantity;
    
    private Instant releaseDate;
}
