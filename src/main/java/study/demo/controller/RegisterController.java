package study.demo.controller;

import javax.servlet.http.HttpServletRequest;
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
import study.demo.service.dto.request.RegisterRequestDto;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    
    // register member 
    @PostMapping("")
    public ResponseEntity<MessageResponseDto> register(@RequestBody @Valid RegisterRequestDto request,HttpServletRequest httpRequest) {
        log.info("Registration is processing...");
        return ResponseEntity.ok(registerService.register(request,httpRequest));
    }
    
    // confirm with link to activate account
    @GetMapping("/confirmationLink")
    public ResponseEntity<Object> confirmByLink(@RequestParam("verificationCode") String verifyCode,WebRequest request) {
        log.info("Confirmation with link is processing...");
        return ResponseEntity.ok(registerService.confirmLink(verifyCode));
    }
    
    // resends new link for user in case the old link was expired 
    @GetMapping("/resendLink")
    public ResponseEntity<Object> resendNewLink(@RequestParam("userName") String userName,HttpServletRequest httpRequest) {
        log.info("Resending new link...");
        return ResponseEntity.ok(registerService.resendNewLink(httpRequest, userName));
    }
    
    // confirm with OTP to activate account
    @GetMapping("/confirmationOtp")
    public ResponseEntity<Object> confirmByOtp(@RequestParam("otp") String otpCode,WebRequest request) {
        log.info("Confirmation with OTP is processing...");
        return ResponseEntity.ok(registerService.confirmOtp(otpCode));
    }
    
    // resends new OTP for user in case the old OTP was expired 
    @GetMapping("/resendOtp")
    public ResponseEntity<Object> resendNewOTP(@RequestParam("userName") String userName,HttpServletRequest httpRequest) {
        log.info("Resending new OTP...");
        return ResponseEntity.ok(registerService.resendNewOtp(httpRequest,userName));
    }

}
