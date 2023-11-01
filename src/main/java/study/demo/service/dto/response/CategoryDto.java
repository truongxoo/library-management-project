package study.demo.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Integer categoryId;

    private String categoryName;

}
