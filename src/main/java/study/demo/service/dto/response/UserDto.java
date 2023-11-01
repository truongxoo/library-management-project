package study.demo.service.dto.response;

import java.time.Instant;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private String gender;

    private Instant birthday;

    private String contact;

}
