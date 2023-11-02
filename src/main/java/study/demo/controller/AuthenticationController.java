package study.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.repository.UserRepository;
import study.demo.service.LogoutService;
import study.demo.service.dto.request.AuthenticationRequest;
import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.request.TokenRefreshRequest;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.impl.AuthenticationServiceImpl;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationServiceImpl authenService;
    
    private final LogoutService logoutService;

    private final UserRepository userRepository;
    
    // Login with username and password
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequest request)
            throws UsernameNotFoundException, Exception {
        log.info("Authentication is processing..");
        return ResponseEntity.ok(authenService.authenticate(request));
    }
    
    // Logout 
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponseDto> logoutPage(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok(new MessageResponseDto("Logout successfully")); 
    }
    
    // Provide new access token if refresh token is valid
    @PostMapping("/refreshtoken")
    public ResponseEntity<AuthenticationResponseDto> refreshtoken(HttpServletRequest request) {
        return ResponseEntity.ok(authenService.refreshtoken(request));
    }

}