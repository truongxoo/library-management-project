package study.demo.service.impl;

import java.time.Instant;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.entity.UserSession;
import study.demo.repository.UserRepository;
import study.demo.security.UserDetailImpl;
import study.demo.security.jwt.JwtService;
import study.demo.service.AuthenticationService;
import study.demo.service.UserSessionService;
import study.demo.service.dto.request.AuthenticationRequestDto;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserSessionService userSessionService;

    private final UserRepository userRepo;

    private final JwtService jwtService;

    private final MessageSource messages;

    // verify users before being allowed to access resources
    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request)
            throws UsernameNotFoundException, Exception {
        UserDetailImpl userDetails = null;

        if (authenticate(request.getEmail(), request.getPassword())) {
            userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername(request.getEmail());
            
            final String accessToken = jwtService.generateTokenFromUserName(userDetails.getUsername());
            final String refreshToken = jwtService.generateRefreshToken(accessToken);    // access token and refresh token
                                                                                         // have the same jti
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new DataInvalidException(messages.getMessage(
                            "user.notfound", new Object[] { request.getEmail() }, Locale.getDefault()),"user.notfound"));
            userSessionService.save(UserSession.builder()
                    .user(user)
                    .userSessionId(jwtService.extractJti(accessToken))
                    .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                    .build());
            return new AuthenticationResponseDto(accessToken, refreshToken);
        }
        return null;

    }

    // using authentication manager to verify user
    public boolean authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            log.error("Wrong password or username");
            throw new CusBadCredentialsException(messages.getMessage("bad.credential", null, Locale.getDefault()),"bad.credential");
        }catch (DisabledException e) {
            log.error("User does not allow to login");
            throw new DataInvalidException(messages.getMessage("user.notactived", null, Locale.getDefault()),"user.notactived");
        } 
        return true;
    }

    // providing new access token if refresh token request is valid
    @Override
    public AuthenticationResponseDto refreshtoken(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization").substring(7);
        if (!jwtService.isRefrehToken(jwt)) { // ~Boolean.FALSE.equals(jwtService.isRefrehToken(jwt))
            throw new DataInvalidException(messages.getMessage("isnot.refreshtoken", null, Locale.getDefault()),"isnot.refreshtoken");
        }
        String jti = jwtService.extractJti(jwt);

        return userSessionService.findByUserSessionId(jti)
                .map(UserSession::getUser)
                .map(user -> {
            userSessionService.verifyExpiration(jti);
            String accessToken = jwtService.generateTokenFromUserName(user.getEmail()); // response new access token
                                                                                        // when
            return new AuthenticationResponseDto(accessToken, jwt); // refresh token is valid
        }).orElseThrow(() -> new VerifyExpirationException(messages.getMessage("refreshtoken.expired", null, Locale.getDefault()),"refreshtoken.expired"));
    }

}
