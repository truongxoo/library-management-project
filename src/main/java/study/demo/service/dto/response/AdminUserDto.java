package study.demo.service.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
    
    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private String gender;

    private Integer borrowerRecord;
    
    private Instant birthDay;
    
    private String phone;
    
    private String userStatus;

    private boolean locked;
}
