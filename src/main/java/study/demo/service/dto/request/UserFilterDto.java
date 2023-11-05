package study.demo.service.dto.request;

import java.time.Instant;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import study.demo.enums.EGender;

@Data
@Builder
public class UserFilterDto {

    private String email;
    
    private String contact;
    
    private String firstName;
    
    private String lastName;
    
    private Instant birthday;
    
    private EGender gender;

}
