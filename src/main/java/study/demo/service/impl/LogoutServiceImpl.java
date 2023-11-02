package study.demo.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import study.demo.security.jwt.JwtService;
import study.demo.service.LogoutService;
import study.demo.service.UserSessionService;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    
    private final UserSessionService userSessionService;
    
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String jwt =  request.getHeader("Authorization").substring(7);
        String jti= jwtService.extractJti(jwt) ;
        userSessionService.deleteByUserSessionId(jti);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
