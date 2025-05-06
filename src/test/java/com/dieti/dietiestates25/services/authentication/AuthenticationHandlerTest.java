package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.services.session.SessionManager;
import com.dieti.dietiestates25.services.session.UserSession;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dieti.dietiestates25.utils.TestUtils.mockResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationHandlerTest {

    @Mock
    private UI ui;

    @Mock
    private AuthenticationService service;

    @InjectMocks
    private AuthenticationHandler handler;

    @BeforeEach
    void setUp() {
        UI.setCurrent(ui);

        handler = new AuthenticationHandler();
        handler.authenticationService = service;
    }

    @Test
    void testConfirmUser_success() {
        var success = mockResponse(true);
        when(service.confirmUser(any(), any()))
                .thenReturn(success);

        handler.confirmUser("user@test.com", "otp");

        verify(service, atMostOnce()).confirmUser(any(), any());
    }
    
    @Test
    void testConfirmUser_failure() {
        var failure = mockResponse(false);
        when(service.confirmUser(any(), any()))
                .thenReturn(failure);
        
        handler.confirmUser("user@test.com", "otp");
        
        verify(service, atMostOnce()).confirmUser(any(), any());
    }
    
    @Test
    void testConfirmUser_nullResponse() {
        when(service.confirmUser(any(), any()))
                .thenReturn(null);
        
        handler.confirmUser("user@test.com", "otp");
        
        verify(service, atMostOnce()).confirmUser(any(), any());
    }

    @Test
    void testLogin_success() {
        var success = mockResponse(true);
        when(service.login(any(), any()))
                .thenReturn(success);

        try (
                var sessionManagerMock = mockStatic(SessionManager.class);
                var sessionMock = mockStatic(UserSession.class)
        ) {
            sessionMock.when(UserSession::getFirstName)
                    .thenReturn("Mock First Name");

            handler.login("user@test.com", "pwd");

            verify(service, atMostOnce()).login(any(), any());
            sessionManagerMock.verify(() -> SessionManager.monitorSession(any()), atMostOnce());
        }
    }

    @Test
    void testLogin_failure() {
        var failure = mockResponse(false);
        when(service.login(any(), any()))
                .thenReturn(failure);

        handler.login("user@test.com", "pwd");

        verify(service, atMostOnce()).login(any(), any());
    }

    @Test
    void testCreateUser_success() {
        var success = mockResponse(true);
        when(service.createUser(any()))
                .thenReturn(success);

        handler.createUser("John", "Doe", "user@test.com", "pwd");

        verify(service, atMostOnce()).createUser(any());
    }

    @Test
    void testCreateUser_failure() {
        var failure = mockResponse(false);
        when(service.createUser(any()))
                .thenReturn(failure);

        handler.createUser("John", "Doe", "user@test.com", "pwd");

        verify(service, atMostOnce()).createUser(any());
    }

    @Test
    void testCreateUser_nullResponse() {
        when(service.createUser(any()))
                .thenReturn(null);

        handler.createUser("John", "Doe", "user@test.com", "pwd");

        verify(service, atMostOnce()).createUser(any());
    }

    @Test
    void testRecreateUser() {
        when(service.createUser(any()))
                .thenReturn(notNull());

        handler.recreateUser("John", "Doe", "user@test.com", "password");

        verify(service, atMostOnce()).createUser(any());
    }

    @Test
    void testLogout_success() {
        var success = mockResponse(true);
        when(service.logout(any()))
                .thenReturn(success);

        handler.logout("user@test.com");

        verify(service, atMostOnce()).logout(any());
    }

    @Test
    void testLogout_failure() {
        var failure = mockResponse(false);
        when(service.logout(any()))
                .thenReturn(failure);

        handler.logout("user@test.com");

        verify(service, atMostOnce()).logout(any());
    }

    @Test
    void testLogout_nullResponse() {
        when(service.logout(any()))
                .thenReturn(null);

        handler.logout("user@test.com");

        verify(service, atMostOnce()).logout(any());
    }

    @Test
    void testChangePwd_success() {
        var success = mockResponse(true);
        when(service.changePwd(any(), any(), any()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            handler.changePwd("user@test.com", "oldPwd", "newPwd");

            sessionMock.verify(UserSession::clearSession, atMostOnce());
            verify(service, atMostOnce()).changePwd(any(), any(), any());

        }
    }

    @Test
    void testChangePwd_failure() {
        var failure = mockResponse(false);
        when(service.changePwd(any(), any(), any()))
                .thenReturn(failure);

        handler.changePwd("user@test.com", "oldPwd", "newPwd");

        verify(service, atMostOnce()).changePwd(any(), any(), any());
    }

    @Test
    void testChangePwd_nullResponse() {
        when(service.changePwd(any(), any(), any()))
                .thenReturn(null);

        handler.changePwd("user@test.com", "oldPwd", "newPwd");

        verify(service, atMostOnce()).changePwd(any(), any(), any());
    }

    @Test
    void testSendOTP_success() {
        var success = mockResponse(true);
        when(service.sendOTP(any()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            handler.sendOTP("user@test.com");

            sessionMock.verify(() -> UserSession.setSessionId(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setEmail(any()), atMostOnce());
            verify(service, atMostOnce()).sendOTP(any());
        }
    }

    @Test
    void testSendOTP_failure() {
        var failure = mockResponse(false);
        when(service.sendOTP(any()))
                .thenReturn(failure);

        handler.sendOTP("user@test.com");

        verify(service, atMostOnce()).sendOTP(any());
    }

    @Test
    void testSendOTP_nullResponse() {
        when(service.sendOTP(any()))
                .thenReturn(null);

        handler.sendOTP("user@test.com");

        verify(service, atMostOnce()).sendOTP(any());
    }
}