package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.QueryParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginViewTest {

    @Mock
    private AuthenticationHandler authHandlerMock;

    @BeforeEach
    void setup() {
        var routes = new Routes();
        routes.autoDiscoverViews("com.dieti.dietiestates25.views");

        MockVaadin.setup(routes);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        var view = new LoginView();
        UI.getCurrent().add(view);

        assertNotNull(view.emailField);
        assertNotNull(view.passwordField);
        assertNotNull(view.loginButton);
        assertNotNull(view.forgotPasswordLink);
    }

    @Test
    void loginButtonShouldCallAuthHandlerWhenFieldsNotEmpty() {
        var view = new LoginView();
        UI.getCurrent().add(view);

        view.authHandler = authHandlerMock; // replace handler with the mocked one
        view.emailField.setValue("test@example.com");
        view.passwordField.setValue("password123");

        view.loginButton.click();

        verify(authHandlerMock, atMostOnce()).login(view.emailField.getValue(), view.passwordField.getValue());
    }

    @Test
    void emptyFieldsShouldBeMarkedInvalid() {
        var view = new LoginView();
        UI.getCurrent().add(view);

        // all fields are left empty
        view.loginButton.click();

        assertTrue(view.emailField.isInvalid());
        assertTrue(view.passwordField.isInvalid());
    }

    @Test
    void temporaryPasswordShouldBeSetFromQueryParameter() {
        UI.getCurrent().navigate(LoginView.class, new QueryParameters(Map.of("tmpPwd", List.of("test"))));

        assertEquals("test", _get(PasswordField.class).getValue());
    }

    @Test
    void noQueryParameterShouldNotSetTemporaryPassword() {
        UI.getCurrent().navigate(LoginView.class);

        assertTrue(_get(PasswordField.class).getValue().isEmpty());
    }

}