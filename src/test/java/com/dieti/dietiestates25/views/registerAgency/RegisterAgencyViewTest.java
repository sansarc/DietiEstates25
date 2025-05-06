package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
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
class RegisterAgencyViewTest {

    @Mock
    private AgencyRequestsHandler handlerMock;

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
        var view = new RegisterAgencyView();
        UI.getCurrent().add(view);

        assertNotNull(view.agencyName);
        assertNotNull(view.vatNumber);
        assertNotNull(view.firstName);
        assertNotNull(view.lastName);
        assertNotNull(view.email);
        assertNotNull(view.form);
    }

    @Test
    void registerButtonShouldCallAgencyHandlerWhenFieldsNotWEmpty() {
        var view = new RegisterAgencyView();
        UI.getCurrent().add(view);

        view.agencyHandler = handlerMock;
        view.agencyName.setValue("MyAgency");
        view.vatNumber.setValue("LU26375245");
        view.firstName.setValue("John");
        view.lastName.setValue("Doe");
        view.email.setValue("test@example.com");

        view.register.click();

        verify(handlerMock, atMostOnce()).createAgency(view.agencyName.getValue(), view.vatNumber.getValue(), view.firstName.getValue(), view.lastName.getValue(), view.email.getValue());
        assertTrue(view.form.areRequiredFieldsValid());
    }

    @Test
    void emptyFieldsShouldBeMarkedInvalid() {
        var view = new RegisterAgencyView();
        UI.getCurrent().add(view);

        view.register.click();

        assertFalse(view.form.areRequiredFieldsValid());
    }
}