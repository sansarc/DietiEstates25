package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dieti.dietiestates25.constants.Constants.Codes.INTERNAL_SERVER_ERROR;
import static com.dieti.dietiestates25.constants.Constants.Codes.OK;
import static com.dieti.dietiestates25.utils.TestUtils.mockResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    private static final String USER_ENTITY_JSON_MOCK = """
                {
                    "entities": [
                        {
                            "company": "",
                            "companyName": "",
                            "confirmed": true,
                            "email": "mockemail@example.com",
                            "firstName": "MockFirstName",
                            "lastName": "MockLastName",
                            "otp": null,
                            "pwd": null,
                            "userType": ""
                        }
                    ],
                    "message": "mock-message-id"
                }
            """;

    @Mock
    private RequestService request;

    @BeforeEach
    void setUp() {
        service = new AuthenticationService();
        service.requestService = request;
    }

    @Test
    void testLogin_success_noManagerOrAgent() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(OK, USER_ENTITY_JSON_MOCK));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isManagerOrAgent)
                    .thenReturn(false);

            var result = service.login("user@test.com", "password");

            assertNotNull(result);
            assertTrue(result.ok());
            sessionMock.verify(() -> UserSession.init(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setSessionId(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setPwd(any()), never());
            verify(request, atMostOnce()).POST(any(), any());
        }
    }

    @Test
    void testLogin_success_withManagerOrAgent() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(OK, USER_ENTITY_JSON_MOCK));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isManagerOrAgent)
                    .thenReturn(true);

            var result = service.login("user@test.com", "password");

            assertNotNull(result);
            assertTrue(result.ok());
            sessionMock.verify(() -> UserSession.init(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setSessionId(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setPwd(any()), atMostOnce());
            verify(request, atMostOnce()).POST(any(), any());
        }
    }

    @Test
    void testLogin_responseWithoutEntity() {
        var success = mockResponse(true);
        when(request.POST(any(), any()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn(null);

            var result = service.login("user@test.com", "password");

            assertNotNull(result);
            assertTrue(result.ok());
            verify(request, atMostOnce()).POST(any(), any());
            sessionMock.verifyNoInteractions(); // Ensure no session initialization occurred
        }
    }

    @Test
    void testLogin_withManagerOrAgent() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(OK, USER_ENTITY_JSON_MOCK));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isManagerOrAgent)
                    .thenReturn(true);

            var result = service.login("manager@test.com", "password");

            assertNotNull(result);
            assertTrue(result.ok());
            sessionMock.verify(() -> UserSession.init(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setSessionId(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setPwd(any()), atMostOnce());
        }
    }
    @Test
    void testLogin_returnsNull() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.login("user@test.com", "password");

        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any());
    }

    @Test
    void testCreateUser_success() {
        var success = mockResponse(true);
        when(request.POST(any(), any()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            var result = service.createUser(new User("John", "Doe", "user@test.com", "password"));

            assertNotNull(result);
            assertTrue(result.ok());
            sessionMock.verify(() -> UserSession.init(any()), atMostOnce());
            sessionMock.verify(() -> UserSession.setSessionId(any()), atMostOnce());
            verify(request, atMostOnce()).POST(any(), any());
        }
    }

    @Test
    void testCreateUser_returnsNull() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.createUser(new User("John", "Doe", "user@test.com", "password"));

        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any());
    }

    @Test
    void testConfirmUser_success() {
        var success = mockResponse(true);
        when(request.POST(any(), any(), any()))
                .thenReturn(success);

        var result = service.confirmUser("user@test.com", "otp");

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).POST(any(), any(), any());
    }

    @Test
    void testConfirmUser_returnsNull() {
        when(request.POST(any(), any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.confirmUser("user@test.com", "otp");

        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any(), any());
    }

    @Test
    void testLogout_success() {
        var success = mockResponse(true);
        when(request.GET(any(), any()))
                .thenReturn(success);

        var result = service.logout("user@test.com");

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testLogout_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.logout("user@test.com");

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testSendOTP_success() {
        var success = mockResponse(true);
        when(request.GET(any(), any()))
                .thenReturn(success);

        var result = service.sendOTP("user@test.com");

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testSendOTP_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.sendOTP("user@test.com");

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testChangePwd_success() {
        var success = mockResponse(true);
        when(request.PUT(any(), any()))
                .thenReturn(success);

        var result = service.changePwd("user@test.com", "oldPwd", "newPwd");

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).PUT(any(), any());
    }

    @Test
    void testChangePwd_returnsNull() {
        when(request.PUT(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.changePwd("user@test.com", "oldPwd", "newPwd");

        assertNull(result);
        verify(request, atMostOnce()).PUT(any(), any());
    }
}