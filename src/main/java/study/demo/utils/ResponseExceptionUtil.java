package study.demo.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import study.demo.service.dto.response.ExceptionMessageDto;

@Component
public class ResponseExceptionUtil {

    public void responseException(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus,
            String message,String messageCode) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        ExceptionMessageDto errorResponse = ExceptionMessageDto
                .builder().statusCode(httpStatus)
                .message(message)
                .messageCode(messageCode)
                .timestamp(null)
                .build();
        byte[] body;
        try {
            body = new ObjectMapper().writeValueAsBytes(errorResponse);
            response.getOutputStream().write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
