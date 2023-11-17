package study.demo.service.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequestDto {

    @NotBlank(message = "{title.notblank}")
    @Size(min = 1, max = 50, message = "{title.length.invalid}")
    private String title;

    @NotBlank(message = "{edition.notblank}")
    @Size(min = 1, max = 50, message = "{edition.length.invalid}")
    private String edition;

    @NotNull(message = "{price.notnull}")
    private BigDecimal price;

    @NotBlank(message = "{category.notblank}")
    @Size(min = 1, max = 50,message ="{category.length.invalid}")
    private String category;

    @NotBlank(message = "{author.notblank}")
    @Size(min = 1, max = 50,message ="{author.length.invalid}")
    private String author;

    private String image;

    @NotNull(message = "{quantity.notnull}")
    private Integer quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Instant releaseDate;

}
