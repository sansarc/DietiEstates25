package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dieti.dietiestates25.constants.Constants.Codes.INTERNAL_SERVER_ERROR;
import static com.dieti.dietiestates25.constants.Constants.Codes.OK;
import static com.dieti.dietiestates25.utils.TestUtils.SUCCESS_JSON_MOCK;
import static com.dieti.dietiestates25.utils.TestUtils.mockResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgencyRequestsServiceTest {

    @InjectMocks
    private AgencyRequestsService service;

    @Mock
    private RequestService request;

    @BeforeEach
    void setUp() {
        service = new AgencyRequestsService();
        service.requestService = request;
    }

    private final String validVAT = "LU26375245";

    @Test
    void testCreateAgency_success() {
        var success = mockResponse(true);
        when(request.POST(any(), any())).thenReturn(success);

        var agency = new RegisterAgency("MyAgency", validVAT, new User("John", "Doe", "john@example.com", null, "MyAgency"));
        var result = service.createAgency(agency);

        assertTrue(result.ok());
        verify(request, atMostOnce()).POST(any(), any());
    }

    @Test
    void testCreateAgency_returnsNull() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.createAgency(new RegisterAgency());
        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any());

    }

    @Test
    void testConfirmManagerOrAgentAccount_success() {
        var success = mockResponse(true);
        when(request.POST(any(), any(), any()))
                .thenReturn(success);

        var result = service.confirmManagerOrAgentAccount("user@test.com", "oldPwd", "newPwd");

        assertTrue(result.ok());
        verify(request, atMostOnce()).POST(any(), any(), any());
    }

    @Test
    void confirmManagerOrAgentAccount_returnsNull() {
        when(request.POST(any(), any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.confirmManagerOrAgentAccount("user@test.com", "oldPwd", "newPwd");

        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any(), any());
    }

    @Test
    void testCreateAgent_success() {
        var success = mockResponse(true);
        when(request.POST(any(), any(), any(), any()))
                .thenReturn(success);

        try (var session = mockStatic(UserSession.class)) {
            session.when(UserSession::getSessionId).thenReturn("mockSessionId");
            var result = service.createAgent(new User("John", "Doe", "john@example.com"));

            assertTrue(result.ok());
        }

        verify(request, atMostOnce()).POST(any(), any(), any(), any());
    }

    @Test
    void testCreateAgent_returnsNull() {
        when(request.POST(any(), any(), any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        try(var session = mockStatic(UserSession.class)) {
            session.when(UserSession::getSessionId).thenReturn("mockSessionId");
            var result = service.createAgent(new User());

            assertNull(result);
        }

        verify(request, atMostOnce()).POST(any(), any(), any(), any());
    }

    @Test
    void testGetAgents_success() {
        var response = new SimpleResponse(OK, SUCCESS_JSON_MOCK);
        var spy = spy(response);

        when(request.GET(any(), any())).thenReturn(spy);

        var result = service.getAgents(validVAT);

        assertTrue(result.ok());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetAgents_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getAgents(validVAT);

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }
}