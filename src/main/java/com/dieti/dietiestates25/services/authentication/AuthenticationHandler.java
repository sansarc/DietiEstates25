package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.dto.SessionResponse;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.signup.OtpView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationHandler {
    private final AuthenticationService authenticationService;
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    public AuthenticationHandler() {
        authenticationService = new AuthenticationService();
    }

    public void confirmUser(String email, String otp) {
        Response confirmed = authenticationService.confirmUser(email, otp);
        VaadinSession.getCurrent().setAttribute("email", null);

        try {
            if (confirmed.ok()) {
                NotificationFactory.success(confirmed.getMessage());
                UI.getCurrent().navigate(LoginView.class);
                logger.info("User confirmed OTP with email: {}", email);
            } else {
                NotificationFactory.error(confirmed.getMessage());
                logger.warn("User wasn't confirmed with email: {}", email);
            }
        } catch (RuntimeException e) {
            logger.error("Critical error while confirming user.", e);
            NotificationFactory.critical();
        }
    }

    public void _login(String email, String password) {
        SessionResponse authenticated = authenticationService.login(email, password);
        try {
            if (authenticated.ok()) {
                NotificationFactory.success("Welcome Back!");
                UI.getCurrent().navigate(HomeView.class);
                logger.info("User logged in with email: {}", email);
            } else {
                logger.warn("User was unable to log in with email: {}", email);
                NotificationFactory.error(authenticated.getMessage());
            }

        } catch (RuntimeException e) {
            logger.error("Critical error while logging in.", e);
            NotificationFactory.critical();
        }
    }

    public void signup(String firstName, String lastName, String email, String password) {
        Response signed = authenticationService.createUser(firstName, lastName, email, password);
        VaadinSession session = VaadinSession.getCurrent();

        try {
            if (session == null) {
                session = new VaadinSession(VaadinService.getCurrent());
                VaadinSession.setCurrent(session);
            }
            if (signed.getStatusCode() == Constants.Codes.CREATED) {
                session.setAttribute("email", email);
                UI.getCurrent().navigate(OtpView.class);
                logger.info("User signed with email: {}", email);
            }
            else {
                NotificationFactory.error(signed.getMessage());
                logger.warn("User attempted to sign with email: {}", email);
            }
        } catch (RuntimeException e) {
            logger.error("Critical error while signing up.", e);
            NotificationFactory.critical();
        }
    }
}
