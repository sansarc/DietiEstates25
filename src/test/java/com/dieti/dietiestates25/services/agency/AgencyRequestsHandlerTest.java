package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.dieti.dietiestates25.utils.TestUtils.mockResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgencyRequestsHandlerTest {

    @Mock
    private UI ui;

    @Captor
    private ArgumentCaptor<Class<? extends Component>> navigationCaptor;

    @Mock
    private AgencyRequestsService service;

    @InjectMocks
    private AgencyRequestsHandler handler;

    @BeforeEach
    void setUp() {
        UI.setCurrent(ui);

        handler = new AgencyRequestsHandler();
        handler.agencyRequestsService = service;
    }

    private void verifyNavigationTo(Class<? extends Component> target) {
        verify(ui, atMostOnce()).navigate(navigationCaptor.capture());
        assertEquals(target, navigationCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    private void verifyNoNavigation() {
        verify(ui, never()).navigate((Class<? extends Component>) any());
    }

    @Test
    void testCreateAgency_success() {
        var success = mockResponse(true);

        when(service.createAgency(any()))
                .thenReturn(success);

        handler.createAgency("FailAgency", "999", "Alice", "Smith", "alice@example.com");

        verify(service, atMostOnce()).createAgency(any());
        verifyNavigationTo(LoginView.class);
    }

    @Test
    void testCreateAgency_failure() {
        var failure = mockResponse(false);

        when(service.createAgency(any()))
                .thenReturn(failure);

        handler.createAgency("FailAgency", "999", "Alice", "Smith", "alice@example.com");

        verify(service).createAgency(any());
        verifyNoNavigation();
    }

    @Test
    void testCreateAgency_nullResponse() {
        when(service.createAgency(any()))
                .thenReturn(null);

        handler.createAgency("FailAgency", "999", "Alice", "Smith", "alice@example.com");

        verify(service).createAgency(any());
        verifyNoNavigation();
    }

    @Test
    void testConfirmManagerOrAgentAccount_whenAgent_success() {
        var success = mockResponse(true);

        when(service.confirmManagerOrAgentAccount(any(), any(), any()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isAgent).thenReturn(true);

            handler.confirmManagerOrAgentAccount("agent@example.com", "temp123", "newpass");
            verify(service).confirmManagerOrAgentAccount(any(), any(), any());
        }
    }

    @Test
    void testConfirmManagerOrAgentAccount_whenManager_success() {
        var success = mockResponse(true);

        when(service.confirmManagerOrAgentAccount(any(), any(), any()))
                .thenReturn(success);

        try (MockedStatic<UserSession> sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isManager).thenReturn(true);

            handler.confirmManagerOrAgentAccount("agent@example.com", "temp123", "newpass");
            verify(service).confirmManagerOrAgentAccount(any(), any(), any());
        }
    }


    @Test
    void testConfirmManagerOrAgentAccount_failure() {
        var failure = mockResponse(false);

        when(service.confirmManagerOrAgentAccount(any(), any(), any()))
                .thenReturn(failure);

        // No UserSession check needed, this won't reach it
        handler.confirmManagerOrAgentAccount("fail@dieti.com", "temp123", "temp123");
        verify(service, atMostOnce()).confirmManagerOrAgentAccount(any(), any(), any());
    }

    @Test
    void testCreateAgent_success() {
        var success = mockResponse(true);

        when(service.createAgent(any()))
                .thenReturn(success);

        handler.createAgent("Eve", "Stone", "eve@example.com");
        verify(service, atMostOnce()).createAgent(any());
    }

    @Test
    void testCreateAgent_failure() {
        var failure = mockResponse(false);

        when(service.createAgent(any()))
                .thenReturn(failure);

        handler.createAgent("Eve", "Stone", "eve@fail.com");
        verify(service, atMostOnce()).createAgent(any());
    }

    @Test
    @SuppressWarnings("unchecked")    // it's just mocking
    void testGetAgents_success() {
        var agents = List.of(new User("A", "B", "ab@test.com"));

        var response = mock(EntityResponse.class);
        when(response.getEntities()).thenReturn(agents);

        when(service.getAgents("123")).thenReturn(response);

        var result = handler.getAgents("123");

        assertNotNull(result);
        verify(service).getAgents("123");
    }

    @Test
    void testGetAgents_nullResponse() {
        when(service.getAgents(any()))
                .thenReturn(null);

        var result = handler.getAgents("fail");
        assertNull(result);
        verify(service, atMostOnce()).getAgents("fail");
    }


}
