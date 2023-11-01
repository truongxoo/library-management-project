package study.demo.service.impl;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.UserSession;
import study.demo.repository.RoleRepository;
import study.demo.repository.UserRepository;
import study.demo.security.UserDetailImpl;
import study.demo.security.jwt.JwtService;
import study.demo.service.AuthenticationService;
import study.demo.service.UserService;
import study.demo.service.UserSessionService;
import study.demo.service.dto.request.AuthenticationRequest;
import study.demo.service.dto.request.TokenRefreshRequest;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.TokenRefreshException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final UserSessionService userSessionService;

    private final UserService userService;

    private final PasswordEncoder encode;

    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequest request)
            throws UsernameNotFoundException, Exception {
        if (authenticate(request.getEmail(), request.getPassword())) {
            UserDetailImpl userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername(request.getEmail());
            final String accessToken = jwtService.generateTokenFromUserName(userDetails.getUsername());
            userSessionService.save(new UserSession().builder()
                    .user(userService.findByEmail(request.getEmail()).get())
                    .userSessionId(jwtService.extractJti(accessToken))
                    .createdDate(Instant.now())
                    .build());
            return new AuthenticationResponseDto(accessToken, jwtService.generateRefreshToken(accessToken));
        } else {
            String msg = "Incorrect username or password, please login again!";
            return new AuthenticationResponseDto(null, msg);
        }
    }

    private boolean authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return true;
        } catch (DisabledException e) {
            log.error("Wrong username!");
        } catch (BadCredentialsException e) {
            log.error("Wrong password!");
            throw new CusBadCredentialsException("Incorrect username or password, please login again!");
        }
        return false;
    }

    @Override
    public AuthenticationResponseDto refreshtoken(TokenRefreshRequest request) {
        return userSessionService.findByUserSessionId(request.getRefreshToken()).map(UserSession::getUser).map(user -> {
            String accessToken = jwtService.generateTokenFromUserName(user.getEmail());
            return new AuthenticationResponseDto(accessToken, null);
        }).orElseThrow(() -> new TokenRefreshException(request.getRefreshToken(), "Refresh token is not in database!"));
    }

}
