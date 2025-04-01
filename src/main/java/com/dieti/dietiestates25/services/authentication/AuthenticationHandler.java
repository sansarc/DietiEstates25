package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.dto.UserSession;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.signup.OtpView;
import com.vaadin.flow.component.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationHandler {
    private final AuthenticationService authenticationService;
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    public AuthenticationHandler() {
        authenticationService = new AuthenticationService();
    }

    public void confirmUser(String email, String otp, boolean isManagerOrAgent) {
        SimpleResponse confirmed = authenticationService.confirmUser(email, otp, isManagerOrAgent);

        if (confirmed == null) {
            NotificationFactory.criticalError();
            return;
        }

        if (confirmed.ok()) {
            NotificationFactory.success("Sign up successful! You can login now.");
            UI.getCurrent().navigate(LoginView.class);
            logger.info("User confirmed OTP with email: {}", email);
        } else {
            NotificationFactory.error(confirmed.getRawBody());
            logger.warn("User wasn't confirmed with email: {}", email);
        }
    }

    public void login(String email, String password) {
        SimpleResponse authenticated = authenticationService.login(email, password);

            if (authenticated == null) {
                NotificationFactory.criticalError();
                return;
            }

            if (authenticated.ok()) {
                NotificationFactory.success(String.format("Welcome Back, %s!", UserSession.getFirstName()));
                UI.getCurrent().navigate(HomeView.class);
                logger.info("User logged in with email: {}", email);
            } else {
                logger.warn("User was unable to log in with email: {}", email);
                NotificationFactory.error("Invalid credentials.");
            }
    }

    public void createUser(String firstName, String lastName, String email, String password) {
        SimpleResponse signed = authenticationService.createUser(
                new User(firstName, lastName, email, password)
        );

        if (signed == null) {
            NotificationFactory.criticalError();
            return;
        }

        if (signed.ok()) {
            logger.info("User signed with email: {}", email);
            UI.getCurrent().navigate(OtpView.class);
        }
        else {
            NotificationFactory.error(signed.getRawBody());
            logger.warn("User attempted to sign with email: {}", email);
        }
    }

    public void recreateUser(String firstName, String lastName, String email, String password) {
        SimpleResponse signed = authenticationService.createUser(
                new User(firstName, lastName, email, password)
        );

        if (signed == null)
            NotificationFactory.criticalError();

    }
}
