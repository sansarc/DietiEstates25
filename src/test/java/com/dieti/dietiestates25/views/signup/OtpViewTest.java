package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.TestUtils;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpViewTest {

    private OtpView view;

    @Mock
    private BeforeEnterEvent beforeEnterMock;

    @Mock
    private AuthenticationHandler authHandlerMock;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();

        view = new OtpView();
        view.authHandler = authHandlerMock;

        UserSession.init(TestUtils.TEST_USER);
        UserSession.setSessionId("mock-session");
    }

    @AfterEach
    void tearDown() {
        UserSession.clearSession();
        MockVaadin.tearDown();
    }

    /*
     * Components are initialized and rendered from beforeEvent implementation
     * and not from the constructor like in other views
     */

    @Test
    void keyComponentsShouldBeInitialized_confirmAccount() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "confirmAccount"));

        view.beforeEnter(beforeEnterMock);

        assertNotNull(view.resendLink);
        assertNotNull(view.otpTextField);
    }

    @Test
    void keyComponentsShouldBeInitialized_changePassword() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "changePassword"));

        view.beforeEnter(beforeEnterMock);

        assertNotNull(view.form);
        assertNotNull(view.resendLink);
    }

    @Test
    void emptyOtpShouldBeMarkedInvalid() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "confirmAccount"));

        view.beforeEnter(beforeEnterMock);
        view.confirmButton.click();

        assertTrue(view.otpTextField.isInvalid());
        verifyNoInteractions(authHandlerMock);
    }

    @Test
    void confirmButtonShouldCallAuthHandlerWhenOtpIsValid() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "confirmAccount"));

        view.beforeEnter(beforeEnterMock);

        view.otpTextField.setValue("123456");
        view.confirmButton.click();

        verify(authHandlerMock, atMostOnce()).confirmUser(UserSession.getEmail(), view.otpTextField.getValue());
    }

    @Test
    void emptyOrInvalidPasswordFieldsShouldMeMarkedInvalid() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "changePassword"));

        view.beforeEnter(beforeEnterMock);

        // empty password fields
        view.otpTextField.setValue("123456");
        view.confirmButton.click();

        assertFalse(view.form.areRequiredFieldsValid());
        verifyNoInteractions(authHandlerMock);

        // mismatched password fields
        view.password.setValue("new");
        view.confirmPassword.setValue("wrongOnPurpose");

        view.confirmButton.click();

        assertFalse(view.form.areRequiredFieldsValid());
        verifyNoInteractions(authHandlerMock);
    }

    @Test
    void confirmButtonShouldCallAuthHandlerWhenFormIsValid() {
        when(beforeEnterMock.getRouteParameters())
            .thenReturn(new RouteParameters("key", "changePassword"));

        view.beforeEnter(beforeEnterMock);

        view.otpTextField.setValue("123456");
        view.password.setValue("<PASSWORD>");
        view.confirmPassword.setValue("<PASSWORD>");

        view.confirmButton.click();

        verify(authHandlerMock, atMostOnce()).confirmUser(UserSession.getEmail(), view.otpTextField.getValue());
    }

    @Test
    void resendLinkShouldCallRecreateUserWhenKeyIsConfirmAccount() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "confirmAccount"));

        view.beforeEnter(beforeEnterMock);
        view.resendLink.action.run();

        verify(authHandlerMock).recreateUser(
                UserSession.getFirstName(),
                UserSession.getLastName(),
                UserSession.getEmail(),
                UserSession.getPwd()
        );
    }

    @Test
    void resendLinkShouldCallSendOTPWhenKeyIsChangePassword() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "changePassword"));

        view.beforeEnter(beforeEnterMock);
        view.resendLink.action.run();

        verify(authHandlerMock).sendOTP(UserSession.getEmail());
    }

    @Test
    void beforeEnterShouldForwardToPageNotFoundWhenKeyIsInvalidOrMissing() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("key", "invalid"));

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock, atMostOnce()).forwardTo(PageNotFoundView.class);
        verifyNoInteractions(authHandlerMock);
    }
}