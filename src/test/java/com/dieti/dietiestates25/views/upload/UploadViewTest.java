package com.dieti.dietiestates25.views.upload;

import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.TestUtils;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.upload.forms.DescriptionNMediaForm;
import com.dieti.dietiestates25.views.upload.forms.DetailsForm;
import com.dieti.dietiestates25.views.upload.forms.GeneralInfoForm;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.github.mvysny.kaributesting.v10.pro.ConfirmDialogKt;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeLeaveEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadViewTest {

    private static Routes routes;
    private UploadView view;

    @Mock private GeneralInfoForm generalInfoForm;
    @Mock private DetailsForm detailsForm;
    @Mock private DescriptionNMediaForm descriptionNMediaForm;

    @Mock
    private AdRequestsHandler handlerMock;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);

        UserSession.init(TestUtils.newUser("A"));
        UserSession.setSessionId("mock-session");

        view = new UploadView();
        view.generalInfoForm = generalInfoForm;
        view.detailsForm = detailsForm;
        view.descriptionNMediaForm = descriptionNMediaForm;
        view.adRequestsHandler = handlerMock;
    }

    @AfterEach
    void tearDown() {
        UserSession.clearSession();
        MockVaadin.tearDown();
    }

    @Test
    void testKeyComponentsShouldBeInitialized() {
        assertNotNull(view.generalInfoForm);
        assertNotNull(view.detailsForm);
        assertNotNull(view.descriptionNMediaForm);
    }

    @Test
    void testClickContinueButtonShouldChangeTab() {
        when(generalInfoForm.areRequiredFieldsValid()).thenReturn(true);
        doNothing().when(generalInfoForm).addFormValues(any());

        when(detailsForm.areRequiredFieldsValid()).thenReturn(true);
        doNothing().when(detailsForm).addFormValues(any());

        assertEquals(view.generalInfoTab, view.tabs.getSelectedTab());

        getCotinueButton().click();   //continueButton
        assertEquals(view.detailsTab, view.tabs.getSelectedTab());

        getCotinueButton().click();
        assertEquals(view.descriptionNMediaTab, view.tabs.getSelectedTab());

    }

    private Button getCotinueButton() {
        var stepsLayout = (VerticalLayout) view.getComponentAt(1);
        return (Button) stepsLayout.getComponentAt(1) // tabsContent (Div)
                .getChildren()
                .map(VerticalLayout.class::cast)
                .toList()
                .getFirst() // GeneralInfoContent (VerticalLayout)
                .getChildren()
                .map(HorizontalLayout.class::cast)
                .toList()
                .getLast()     // buttonsLayout
                .getComponentAt(1);
    }

    private Button getBackButton() {
        var stepsLayout = (VerticalLayout) view.getComponentAt(1);
        return (Button) stepsLayout.getComponentAt(1) // tabsContent (Div)
                .getChildren()
                .map(VerticalLayout.class::cast)
                .toList()
                .getFirst() // GeneralInfoContent (VerticalLayout)
                .getChildren()
                .map(HorizontalLayout.class::cast)
                .toList()
                .getLast()     // buttonsLayout
                .getComponentAt(0);
    }

    @Test
    void testClickBackButtonShouldChangeTab() {
        view.tabs.setSelectedTab(view.descriptionNMediaTab);

        getBackButton().click();
        assertEquals(view.detailsTab, view.tabs.getSelectedTab());

        getBackButton().click();
        assertEquals(view.generalInfoTab, view.tabs.getSelectedTab());
    }

    @Test
    void testClickButtonOnFirstTabShowsNotification() {
        view.tabs.setSelectedTab(view.generalInfoTab);

        getBackButton().click();

        NotificationsKt.expectNotifications("You're at the beginning of the form");
    }

    @Test
    void continueButtonOpensConfirmDialog() {
        when(descriptionNMediaForm.areRequiredFieldsValid()).thenReturn(true);
        doNothing().when(descriptionNMediaForm).addFormValuesNPhotos(any(), any());

        view.tabs.setSelectedTab(view.descriptionNMediaTab);
        getCotinueButton().click();

        assertTrue(_get(ConfirmDialog.class).isOpened());
    }
    
    @Test
    void testLeavingWhenFormIsNotEmptyOpensConfirmDialog() {
        // fill a random field
        view.generalInfoForm.getChildren()
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .findAny()
                .ifPresent(field -> field.setValue("123"));

        view.beforeLeave(mock(BeforeLeaveEvent.class));

        assertTrue(_get(ConfirmDialog.class).isOpened());

        ConfirmDialogKt._fireCancel(_get(ConfirmDialog.class));

        assertInstanceOf(HomeView.class, MockedUI.getCurrent().getCurrentView());
    }
}