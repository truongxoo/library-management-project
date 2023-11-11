package study.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.demo.service.SMSService;
import study.demo.service.dto.response.MessageResponseDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class SMSController {

    private final SMSService smsService;

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<MessageResponseDto> sendSMS(@RequestParam("phone")String phone) {

        return ResponseEntity.ok(smsService.sendSMS(phone));
    }
}
