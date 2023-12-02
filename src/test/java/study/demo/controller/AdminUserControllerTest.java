package study.demo.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import study.demo.repository.UserRepository;
import study.demo.service.LogoutService;
import study.demo.service.dto.request.AuthenticationRequestDto;
import study.demo.service.dto.response.AuthenticationResponseDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.impl.AuthenticationServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureMockMvc
public class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    
    private String TOKEN_ATTR_NAME;
    private  HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;
    private CsrfToken csrfToken;

    @BeforeEach
    void init() {
         TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
         httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
         csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
    }

    @Test
    @WithMockUser(username = "member", roles = "USER")
    void AuthenticationController_DeleteUserByIdWithRoleUser_ReturnAccessDenied() throws Exception {
     
        lenient().doNothing().when(userRepository).deleteById(anyInt());
        
        MockHttpServletResponse response = mockMvc
                .perform(delete("/api/admin/users/2").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @WithMockUser(username = "admin" ,password = "admin", authorities = "ADMIN")
    void AuthenticationController_DeleteUserByIdWithRoleAdmin_ReturnStatusOK() throws Exception {

        lenient().doNothing().when(userRepository).deleteById(2);

        ResultActions response = mockMvc
                .perform(delete("/api/admin/users/{id}",2)
                        .with(csrf())
                        .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                        .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
