package study.demo.security.jwt;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonParseException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.exception.VerifyExpirationException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;
    
    private final MessageSource messages;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Verifying jwt token....." );
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;
//        boolean isRefrehToken = jwtService.isRefrehToken(jwt);
        
//        if(isRefrehToken) {
//            
//        }
        // jwt must not be null and has to start with 'Bearer'
        if (authHeader != null && authHeader.startsWith("Bearer ")) {  
            jwt = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(jwt);    // get username from token
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
                throw new VerifyExpirationException(messages.getMessage("token.expired", null, Locale.getDefault()));
            } catch (Exception e) {
                log.error("Unable to get JWT Token");
                throw new VerifyExpirationException(messages.getMessage("token.invalid", null, Locale.getDefault()));

            }
        }
        // create authentication object for next process
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
