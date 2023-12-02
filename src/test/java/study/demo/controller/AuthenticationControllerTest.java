package study.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import study.demo.controller.common.AuthenticationController;
import study.demo.service.AuthenticationService;
import study.demo.service.LogoutService;
import study.demo.service.dto.request.AuthenticationRequestDto;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.impl.AuthenticationServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @MockBean
    private LogoutService logoutService;

    private AuthenticationResponseDto authResponse;

    private AuthenticationRequestDto authRequest;

    private String TOKEN_ATTR_NAME;
    private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;
    private CsrfToken csrfToken;

    @BeforeEach
    void init() {
        authResponse = AuthenticationResponseDto.builder().token(
                "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZDhhODg2Ny1lZTE1LTRmYzgtYTM2YS0yNWJhZjQzNjI0MGUiLCJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDA2MjQyOTcsImV4cCI6MTcwMDYyNjA5N30.t_agqoqk1ckSV5OA1Btv6wus9y_pPHYLcVGPeoObKmk")
                .refreshToken(
                        "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZDhhODg2Ny1lZTE1LTRmYzgtYTM2YS0yNWJhZjQzNjI0MGUiLCJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDA2MjQyOTcsImV4cCI6MTcwMDYyNjA5N30.t_agqoqk1ckSV5OA1Btv6wus9y_pPHYLcVGPeoObKmk")
                .build();
        authRequest = AuthenticationRequestDto.builder().email("vantruong2904078@gmail.com").password("vantruong2")
                .build();

        TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
        httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
    }

    @Test
    void AuthenticationController_Authenticate_ReturnStatusOK() throws Exception {
        
         when(authenticationService.authenticate(Mockito.any(AuthenticationRequestDto.class)))
         .thenReturn(authResponse);
        
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(authRequest)));
            response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void AuthenticationController_AuthenticaWithWrongPassword_ReturnBadRequestStatus() throws Exception {
          
         when(authenticationService.authenticate(Mockito.any(AuthenticationRequestDto.class)))
         .thenThrow(DataInvalidException.class);
        
         MockHttpServletResponse response = mockMvc.perform(post("/api/auth/login")
                 .accept(MediaType.APPLICATION_JSON))
                 .andReturn().getResponse();
         Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void AuthenticationController_AuthenticaWithWrongUserName_ReturnBadRequest() throws Exception {
            
            when(authenticationService.authenticate(Mockito.any(AuthenticationRequestDto.class)))
            .thenThrow(CusNotFoundException.class);
            MockHttpServletResponse response = mockMvc.perform(post("/api/auth/login")
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();
            Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser(username = "member", roles = "USER")
    void AuthenticationController_RefreshToken_ReturnStatusOK() throws Exception {
        
        when(authenticationService.refreshtoken(Mockito.any(HttpServletRequest.class)))
        .thenReturn(Mockito.any(AuthenticationResponseDto.class));
        
        MockHttpServletResponse response = mockMvc.perform(post("/api/auth/refreshtoken")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(username = "member", roles = "USER")
    void AuthenticationController_RefreshToken_ReturnBadRequest() throws Exception {
        
        when(authenticationService.refreshtoken(Mockito.any(HttpServletRequest.class)))
        .thenThrow(DataInvalidException.class);
        
        MockHttpServletResponse response = mockMvc.perform(post("/api/auth/refreshtoken")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @WithMockUser(username = "member", roles = "USER")
    void AuthenticationController_doLogout_ReturnStatusOK() throws Exception {
        
        MessageResponseDto message = MessageResponseDto.builder().message("Logout successfully").build();
        when(logoutService.logout(Mockito.any(HttpServletRequest.class),Mockito.any(HttpServletResponse.class)))
        .thenReturn(message);
        
        MockHttpServletResponse response = mockMvc.perform(get("/api/auth/logout")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
