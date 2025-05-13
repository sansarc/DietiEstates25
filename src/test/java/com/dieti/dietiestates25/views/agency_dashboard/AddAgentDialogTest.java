package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.Form;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static com.github.mvysny.kaributesting.v10.NotificationsKt.getNotifications;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AddAgentDialogTest {

    private AddAgentDialog dialog;

    @Mock
    AgencyRequestsHandler handlerMock;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();

        dialog = new AddAgentDialog();
        dialog.agencyRequestsHandler = handlerMock;
        dialog.open();
    }

    @AfterEach
    void tearDown() {
        dialog.close();
        MockVaadin.tearDown();
    }

    @Test
    void testKeyComponentsShouldBeInitialized() {
        assertTrue(dialog.isOpened());
        assertNotNull(_get(Form.class));
        assertNotNull(_get(TextField.class, spec -> spec.withLabel("First Name")));
        assertNotNull(_get(TextField.class, spec -> spec.withLabel("Last Name")));
        assertNotNull(_get(EmailField.class));
        assertEquals(2, _find(Button.class).size());
    }

    @Test
    void testCancelButtonClosesDialog() {
        _get(Button.class, spec -> spec.withText("Cancel")).click();
        assertFalse(dialog.isOpened());
    }

    @Test
    void testAddButtonCallsHandler_invalidForm() {
        _get(Button.class, spec -> spec.withText("Add")).click();

        assertFalse(getNotifications().isEmpty());
        verifyNoInteractions(handlerMock);
    }

    @Test
    void testAddButtonCallsHandler_validForm() {
        _get(TextField.class, spec -> spec.withLabel("First Name")).setValue("John");
        _get(TextField.class, spec -> spec.withLabel("Last Name")).setValue("Doe");
        _get(EmailField.class).setValue("foo@bar.com");

        _get(Button.class, spec -> spec.withText("Add")).click();

        assertTrue(getNotifications().isEmpty());
        verify(handlerMock).createAgent(any(), any(), any());
    }
}