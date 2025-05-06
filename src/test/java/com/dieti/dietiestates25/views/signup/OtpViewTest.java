package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
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
    void keyComponentsShouldBeInitialized_confirmAccount() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "confirmAccount"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        assertNotNull(view.resendLink);
        assertNotNull(view.otpTextField);
    }

    @Test
    void keyComponentsShouldBeInitialized_changePassword() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        assertNotNull(view.form);
        assertNotNull(view.resendLink);
    }

    @Test
    void emptyOtpShouldBeMarkedInvalid() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                            .thenReturn(true);

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "confirmAccount"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        view.authHandler = authHandlerMock;
        view.confirmButton.click();

        assertTrue(view.otpTextField.isInvalid());
        verifyNoInteractions(authHandlerMock);
    }

    @Test
    void confirmButtonShouldCallAuthHandlerWhenOtpIsValid() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);
            sessionMock.when(UserSession::getEmail)
                    .thenReturn("test@example.com");

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "confirmAccount"));
            view = (OtpView) UI.getCurrent().getCurrentView();

            view.authHandler = authHandlerMock;
            view.otpTextField.setValue("123456");
            view.confirmButton.click();

            verify(authHandlerMock, atMostOnce()).confirmUser(UserSession.getEmail(), view.otpTextField.getValue());
        }
    }

    @Test
    void emptyOrInvalidPasswordFieldsShouldMeMarkedInvalid() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);
            sessionMock.when(UserSession::getEmail)
                    .thenReturn("test@example.com");

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        view.authHandler = authHandlerMock;

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
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);
            sessionMock.when(UserSession::getEmail)
                    .thenReturn("test@example.com");

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
            view.authHandler = authHandlerMock;

            view.otpTextField.setValue("123456");
            view.password.setValue("<PASSWORD>");
            view.confirmPassword.setValue("<PASSWORD>");

            view.confirmButton.click();

            verify(authHandlerMock, atMostOnce()).confirmUser(UserSession.getEmail(), view.otpTextField.getValue());
        }
    }

    @Test
    void resendLinkShouldCallRecreateUserWhenKeyIsConfirmAccount() {
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);
            sessionMock.when(UserSession::getEmail)
                    .thenReturn("test@example.com");
            sessionMock.when(UserSession::getFirstName)
                    .thenReturn("John");
            sessionMock.when(UserSession::getLastName)
                    .thenReturn("Doe");
            sessionMock.when(UserSession::getPwd)
                    .thenReturn("password123");

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "confirmAccount"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        view.authHandler = authHandlerMock;

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
        OtpView view;

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);
            sessionMock.when(UserSession::getEmail)
                    .thenReturn("test@example.com");

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
            view.authHandler = authHandlerMock;

            view.resendLink.action.run();

            verify(authHandlerMock).sendOTP(UserSession.getEmail());
        }
    }

    @Test
    void beforeEnterShouldForwardToPageNotFoundWhenKeyIsInvalidOrMissing() {
        OtpView view;

        BeforeEnterEvent eventMock = mock(BeforeEnterEvent.class);
        RouteParameters routeParamsMock = mock(RouteParameters.class);

        when(eventMock.getRouteParameters()).thenReturn(routeParamsMock);
        when(routeParamsMock.get("key")).thenReturn(java.util.Optional.of("invalidKey"));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        view.beforeEnter(eventMock);

        when(eventMock.getRouteParameters()).thenReturn(routeParamsMock);
        when(routeParamsMock.get("key")).thenReturn(java.util.Optional.empty());

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::isUserLoggedIn)   // bypassing @ForwardGuest
                    .thenReturn(true);

            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePassword"));
            view = (OtpView) UI.getCurrent().getCurrentView();
        }

        view.beforeEnter(eventMock);

        verify(eventMock, times(2)).forwardTo(PageNotFoundView.class);
    }
}