package study.demo.security.jwt;

import java.io.IOException;
import java.time.Instant;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.controller.common.ExcptionHandlerController;
import study.demo.service.UserSessionService;
import study.demo.service.dto.response.ExceptionMessageDto;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;
import study.demo.utils.ResponseExceptionUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ResponseExceptionUtil responseExceptionUtil;

    private final UserDetailsService userDetailsService;

    private final UserSessionService userSessionService;

    private final MessageSource messages;

    private final JwtService jwtService;

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Verifying jwt token.....");
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String jti = null;
        String username = null;
        
        // jwt must not be null and has to start with 'Bearer'
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                
                jwt = authHeader.substring(7);
                username = jwtService.extractUsername(jwt); // get username from token
                jti = jwtService.extractJti(jwt);
                
                userSessionService.findByUserSessionId(jti).orElseThrow(() -> new DataInvalidException(
                        messages.getMessage("not.authenticate", null, Locale.getDefault()),"user.not.authenticate"));
                
                // create authentication object for next process
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    String requestPath = request.getRequestURL().toString();

                    if (jwtService.isTokenValid(jwt, userDetails, requestPath)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("JWT Token has expired");
            responseExceptionUtil.responseException(request, response, HttpStatus.UNAUTHORIZED,
                    messages.getMessage("token.expired", null, Locale.getDefault()),"token.expired");
        } catch (DataInvalidException e) {
            log.error("JWT is invalid to access resource");
            responseExceptionUtil.responseException(request, response, HttpStatus.UNAUTHORIZED, e.getMessage(),"unauthorized");
        } catch (Exception e) {
            log.error("Unable to get JWT Token");
            responseExceptionUtil.responseException(request, response, HttpStatus.UNAUTHORIZED,
                    messages.getMessage("token.invalid", null, Locale.getDefault()),"token.invalid");
        }
    }

}
