package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.Form;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
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
class ConfirmAccountDialogTest {

    @Mock
    private AgencyRequestsHandler handlerMock;

    private ConfirmAccountDialog dialog;
    private Form form;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button confirmButton;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();

        dialog = new ConfirmAccountDialog();
        dialog.agencyRequestsHandler = handlerMock;
        dialog.open();

        UI.getCurrent().getInternals().getStateTree().runExecutionsBeforeClientResponse();  // force UI to complete lazy loading before anything else

        form = _get(Form.class);
        passwordField = _get(PasswordField.class, spec -> spec.withLabel("Confirm new password"));
        confirmPasswordField = _get(PasswordField.class, spec -> spec.withLabel("New password"));
        confirmButton = _get(Button.class);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        assertNotNull(passwordField);
        assertNotNull(confirmPasswordField);
        assertNotNull(confirmButton);

        assertFalse(dialog.isCloseOnEsc());
        assertFalse(dialog.isCloseOnOutsideClick());
    }

    @Test
    void confirmButtonShouldCallAgencyHandlerWhenFieldsAreValid() {
        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getEmail).thenReturn("test@example.com");
            sessionMock.when(UserSession::getPwd).thenReturn("tmpPwd");

            passwordField.setValue("new");
            confirmPasswordField.setValue("new");
            confirmButton.click();

            assertTrue(form.areRequiredFieldsValid());
            verify(handlerMock).confirmManagerOrAgentAccount(UserSession.getEmail(), UserSession.getPwd(),passwordField.getValue());
        }
    }

    @Test
    void emptyFieldsShouldNotBeMarkedInvalid() {
        confirmButton.click();

        assertFalse(form.areRequiredFieldsValid());
        verify(handlerMock, never()).confirmManagerOrAgentAccount(any(), any(), any());
    }
}