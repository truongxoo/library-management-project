package study.demo.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.UserInfoService;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.ChangePhoneRequestDto;
import study.demo.service.dto.request.ChangePasswordRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserInformationController {

    private final UserInfoService userService;

    @GetMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> resetUserPassword(HttpServletRequest httpRequest) {
        log.info("Request reset password is processing...");
        return ResponseEntity.ok(userService.resetPassword(httpRequest));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> confirmResetUserPassword(@RequestBody @Valid ResetPasswordDto password,HttpServletRequest httpRequest) {
        log.info("Confirm reset password is processing...");
        return ResponseEntity.ok(userService.confirmResetPassword(httpRequest,password));
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<MessageResponseDto> changeUserPassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        log.info("Request change password is processing...");
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @PostMapping("/change-email")
    public ResponseEntity<MessageResponseDto> changeUserMail(@Valid @RequestBody ChangeMailRequestDto request) {
        log.info("Request change email is processing...");
        return ResponseEntity.ok(userService.changeMail(request));
    }

    @PostMapping("/change-email/confirm")
    public ResponseEntity<MessageResponseDto> confirmChangeMail(@Valid @RequestBody ChangeMailRequestDto request,
            @RequestParam("otp") String otpCode) {
        log.info("Confirm change email...");
        return ResponseEntity.ok(userService.confirmChangeMail(request, otpCode));
    }
    
    @GetMapping("/change-phone")
    public ResponseEntity<MessageResponseDto> requestChangePhone(HttpServletRequest request) {
        log.info("Confirm change email...");
        return ResponseEntity.ok(userService.requestChangePhone());
    }
    @PostMapping("/change-phone/confirm")
    public ResponseEntity<MessageResponseDto> confirmChangPhone(@Valid @RequestBody ChangePhoneRequestDto newPhone,HttpServletRequest request) {
        log.info("Confirm change email...");
        return ResponseEntity.ok(userService.confirmChangePhone(request,newPhone));
    }
    
    
    

}
