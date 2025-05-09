package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginViewTest {

    private LoginView view;

    @Mock
    private AuthenticationHandler authHandlerMock;

    @Mock
    private BeforeEvent beforeEventMock;

    private static Routes routes;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setup() {
        MockVaadin.setup(routes);

        view = new LoginView();
        view.authHandler = authHandlerMock;

        when(beforeEventMock.getLocation())
                .thenReturn(mock(Location.class));
        when(beforeEventMock.getLocation().getQueryParameters())
                .thenReturn(new QueryParameters(Map.of()));
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        view.setParameter(beforeEventMock, null);

        assertNotNull(view.emailField);
        assertNotNull(view.passwordField);
        assertNotNull(view.loginButton);
        assertNotNull(view.forgotPasswordLink);
    }

    @Test
    void loginButtonShouldCallAuthHandlerWhenFieldsNotEmpty() {
        view.setParameter(beforeEventMock, null);

        view.authHandler = authHandlerMock; // replace handler with the mocked one
        view.emailField.setValue("test@example.com");
        view.passwordField.setValue("password123");

        view.loginButton.click();

        verify(authHandlerMock, atMostOnce()).login(view.emailField.getValue(), view.passwordField.getValue());
    }

    @Test
    void emptyFieldsShouldBeMarkedInvalid() {
        view.setParameter(beforeEventMock, null);

        // all fields are left empty
        view.loginButton.click();

        assertTrue(view.emailField.isInvalid());
        assertTrue(view.passwordField.isInvalid());
    }

    @Test
    void temporaryPasswordShouldBeSetFromQueryParameter() {
        when(beforeEventMock.getLocation().getQueryParameters())
                .thenReturn(new QueryParameters(Map.of("tmpPwd", List.of("test"))));

        view.setParameter(beforeEventMock, null);

        assertEquals("test", view.passwordField.getValue());
    }

    @Test
    void noQueryParameterShouldNotSetTemporaryPassword() {
        view.setParameter(beforeEventMock, null);

        assertTrue(view.passwordField.isEmpty());
    }

}