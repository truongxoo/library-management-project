package study.demo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import study.demo.entity.User;
import study.demo.enums.EGender;
import study.demo.repository.UserRepository;
import study.demo.repository.UserSessionRepository;
import study.demo.service.dto.request.ChangePasswordRequestDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.impl.UserInfoServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionRepository userSessionRepository;
    
    @Mock
    private MessageSource messages;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    @MockBean

    private Optional<User> truong;
    private ChangePasswordRequestDto request;
    private ChangePasswordRequestDto request1;

    @BeforeEach
    void init() {
        truong = Optional.of(User.builder().email("vantruong2904078@gmail.com").password("Vantruong2")
                .firstName("Truong").gender(EGender.MALE).build());

        request = ChangePasswordRequestDto.builder().newPassword("Vantruong3").confirmPassword("Vantruong")
                .currentPassword("Vantruong2").build();

        request1 = ChangePasswordRequestDto.builder().newPassword("Vantruong3").confirmPassword("Vantruong3")
                .currentPassword("Vantruong3").build();

    }

    @Test
    void UserInfoService_ChangePasswordWithWrongUserName_ThrowCusNotFoundException() {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken("truong", "123");
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.getUserWithUserSession(anyString())).thenReturn(Optional.ofNullable(null));
            MessageResponseDto response = userInfoService.changePassword(request);
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(CusNotFoundException.class);
        }

    }

    @Test
    void UserInfoService_ChangePasswordWithWrongConfirmPassword_ThrowDataInvalidException() {
        try {
            when(userRepository.getUserWithUserSession(anyString())).thenReturn(truong);
            when(messages.getMessage("cfpassword.notmatch", null, Locale.getDefault())).thenReturn("");
            MessageResponseDto response = userInfoService.changePassword(request);
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(DataInvalidException.class);
        }
    }

    @Test
    void UserInfoService_ChangePasswordWithWrongPassword_ThrowDataInvalidException() {
        try {

            Authentication authentication = new UsernamePasswordAuthenticationToken("truong", "123");
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.getUserWithUserSession(anyString())).thenReturn(truong);
            when(encoder.matches((CharSequence) anyString(), anyString())).thenReturn(false);
            when(messages.getMessage("password.incorrect", null, Locale.getDefault())).thenReturn("");
            MessageResponseDto response = userInfoService.changePassword(request1);
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(DataInvalidException.class);
        }
    }

    @Test
    void UserInfoService_ChangePasswordWithNewPasswordIsSame_ThrowDataInvalidException() {
        try {

            Authentication authentication = new UsernamePasswordAuthenticationToken("truong", "123");
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.getUserWithUserSession(anyString())).thenReturn(truong);
            when(encoder.matches((CharSequence) anyString(), anyString())).thenReturn(true);
            when(messages.getMessage("newpassword.issame", null, Locale.getDefault())).thenReturn("");
            MessageResponseDto response = userInfoService.changePassword(request1);
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(DataInvalidException.class);
        }

    }

    @Test
    void UserInfoService_ChangePassword_ReturnMessageResponseSuccess() {

            Authentication authentication = new UsernamePasswordAuthenticationToken("truong", "123");
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.getUserWithUserSession(anyString())).thenReturn(truong);
            when(userRepository.save(mock(User.class ))).thenReturn(mock(User.class));
            lenient().doNothing().when(userSessionRepository).deleteUserSessionByUserName(anyInt());
            
            when(encoder.matches((CharSequence) anyString(), anyString())).thenReturn(true);
            MessageResponseDto response = userInfoService.changePassword(request1);

    }
}
