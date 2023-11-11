package study.demo.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import study.demo.security.jwt.JwtService;
import study.demo.service.LogoutService;
import study.demo.service.UserSessionService;
import study.demo.service.dto.response.MessageResponseDto;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final UserSessionService userSessionService;

    private final JwtService jwtService;
    
    private final MessageSource messages;

    @Override
    public MessageResponseDto logout(HttpServletRequest request, HttpServletResponse response) {

        String jwt = request.getHeader("Authorization").substring(7);
        String jti = jwtService.extractJti(jwt);
        userSessionService.deleteByUserSessionId(jti);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return new MessageResponseDto(messages.getMessage("logout.success", null,Locale.getDefault()));
    }
}
