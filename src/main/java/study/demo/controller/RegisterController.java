package study.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.RegisterService;
import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@RequestBody @Valid RegisterRequest request,HttpServletRequest httpRequest) {
        log.info("Registration is processing...");
        return ResponseEntity.ok(registerService.register(request,httpRequest));
    }
    
    @GetMapping("/registrationConfirm")
    public ResponseEntity<Object> register(@RequestParam("verifyCode") String verifyCode,WebRequest request) {
        log.info("Verification is processing...");
        return ResponseEntity.ok(registerService.confirmRegistration(verifyCode));
    }

}
