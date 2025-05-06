package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignUpViewTest {

    @Mock
    private AuthenticationHandler authHandlerMock;

    @BeforeEach
    void setUp() {
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
        var view = new SignUpView();
        UI.getCurrent().add(view);

        assertNotNull(view.firstName);
        assertNotNull(view.lastName);
        assertNotNull(view.email);
        assertNotNull(view.password);
        assertNotNull(view.confirmPassword);
        assertNotNull(view.signup);
        assertNotNull(view.form);  // contains all the fields above
    }

    @Test
    void signupButtonShouldCallAuthHandlerWhenFieldsNotEmpty() {
        var view = new SignUpView();
        UI.getCurrent().add(view);

        view.authHandler = authHandlerMock;
        view.firstName.setValue("John");
        view.lastName.setValue("Doe");
        view.email.setValue("test@example.com");
        view.password.setValue("pwd");
        view.confirmPassword.setValue("pwd");

        view.signup.click();

        assertTrue(view.form.areRequiredFieldsValid());
        verify(authHandlerMock, atMostOnce()).createUser(view.firstName.getValue(), view.lastName.getValue(), view.email.getValue(), view.password.getValue());
    }

    @Test
    void emptyFieldsShouldMeMarkedInvalid() {
        var view = new SignUpView();
        UI.getCurrent().add(view);

        // all fields are left empty
        view.signup.click();

        assertFalse(view.form.areRequiredFieldsValid());
    }
}