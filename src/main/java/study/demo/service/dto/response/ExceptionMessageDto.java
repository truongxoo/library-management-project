package study.demo.service.dto.response;

import java.time.Instant;
import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExceptionMessageDto {

    private HttpStatus statusCode;

    private Instant timestamp;

    private String message;

}
