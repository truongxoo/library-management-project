package study.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.LogoutService;
import study.demo.service.dto.request.AuthenticationRequestDto;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.impl.AuthenticationServiceImpl;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenService;
    
    private final LogoutService logoutService;

    // Login with username and password
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequestDto request)
            throws UsernameNotFoundException, Exception {
        log.info("Authentication is processing..");
        return ResponseEntity.ok(authenService.authenticate(request));
    }
    
    // Logout 
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponseDto> logoutPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout is processing..");
        return ResponseEntity.ok(logoutService.logout(request, response)); 
    }
    
    // Provide new access token if refresh token is valid
    @PostMapping("/refreshtoken")
    public ResponseEntity<AuthenticationResponseDto> refreshtoken(HttpServletRequest request) {
        log.info("Refresh token is processing..");
        return ResponseEntity.ok(authenService.refreshtoken(request));
    }

}
