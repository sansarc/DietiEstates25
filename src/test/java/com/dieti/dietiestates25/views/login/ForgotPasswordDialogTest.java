package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.EmailField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordDialogTest {

    @Mock
    private AuthenticationHandler authHandlerMock;

    private ForgotPasswordDialog dialog;
    private EmailField emailField;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();

        dialog = new ForgotPasswordDialog();
        dialog.authenticationHandler = authHandlerMock;
        dialog.open();

        emailField = _get(EmailField.class);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        assertNotNull(emailField);

        assertTrue(dialog.isCloseOnEsc());
        assertTrue(dialog.isCloseOnOutsideClick());
    }

    @Test
    void sendButtonShouldCallAuthHandlerWhenEmailIsValid() {
        emailField.setValue("test@example.com");
        _get(Button.class, spec -> spec.withText("Send")).click();

        assertFalse(emailField.isInvalid());
        verify(authHandlerMock).sendOTP(emailField.getValue());
    }

    @Test
    void emptyEmailShouldBeMarkedInvalid() {
        // empty email
        _get(Button.class, spec -> spec.withText("Send")).click();

        assertTrue(emailField.isInvalid());
        verify(authHandlerMock, never()).sendOTP(any());
    }

    @Test
    void invalidEmailShouldBeMarkedInvalid() {
        emailField.setValue("invalid");
        _get(Button.class, spec -> spec.withText("Send")).click();

        assertTrue(emailField.isInvalid());
        verify(authHandlerMock, never()).sendOTP(any());
    }
}