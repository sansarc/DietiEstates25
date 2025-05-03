package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.logging.Log;
import com.dieti.dietiestates25.services.session.SessionManager;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.DialogUtils;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.signup.OtpView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.util.StringUtils.capitalize;

public class AuthenticationHandler {
    AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    public AuthenticationHandler() {
        authenticationService = new AuthenticationService();
    }

    public void confirmUser(String email, String otp) {
        var confirmed = authenticationService.confirmUser(email, otp);

        if (confirmed == null) {
            return;
        }

        if (confirmed.ok()) {
            NotificationFactory.success("Sign up successful! You can login now.");
            UserSession.clearSession();
            UI.getCurrent().navigate(LoginView.class);
            logger.info("User confirmed OTP with email: {}", email);
        } else {
            NotificationFactory.error(confirmed.getRawBody());
            logger.warn("User wasn't confirmed with email: {}", email);
        }
    }

    public void login(String email, String password) {
        var authenticated = authenticationService.login(email.toLowerCase(), password);

        if (authenticated == null) {
            return;
        }

        if (authenticated.ok()) {
            NotificationFactory.success(String.format("Welcome Back, %s!", UserSession.getFirstName()));
            SessionManager.monitorSession(UI.getCurrent());
            UI.getCurrent().navigate(HomeView.class);
            logger.info("User logged in with email: {}", email);

        } else {
            logger.warn("User was unable to log in with email: {}", email);
            NotificationFactory.error("Invalid credentials.");
        }
    }

    public void createUser(String firstName, String lastName, String email, String password) {
        var user = new User(capitalize(firstName), capitalize(lastName), email, password);
        var signed = authenticationService.createUser(user);

        if (signed == null)
            return;


        if (signed.ok()) {
            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "confirmAccount"));
            logger.info("User signed with email: {}", email);
        }
        else {
            NotificationFactory.error(signed.getRawBody());
            logger.warn("User attempted to sign with email: {}", email);
        }
    }

    public void recreateUser(String firstName, String lastName, String email, String password) {
        authenticationService.createUser(
                new User(firstName, lastName, email, password)
        );
    }

    public void logout(String sessionId) {
        var response = authenticationService.logout(sessionId);

        if (response == null)
            return;

        if (response.ok())
            Log.info(UserSession.class, "Logged out successfully.");
        else
            Log.warn(UserSession.class, "Failed to log out server-side.");
    }

    public void changePwd(String email, String newPwd, String otp) {
        var response = authenticationService.changePwd(email, newPwd, otp);

        if (response == null)
            return;

        if (response.ok()) {
            UserSession.clearSession();
            UI.getCurrent().navigate(LoginView.class);
            NotificationFactory.success("Password change successful! Now you can log back in.");
            Log.info(UserSession.class, email + "changed password successfully.");
        }
        else {
            NotificationFactory.error(response.getRawBody());
            Log.warn(UserSession.class, email + "failed to change password.");
        }
    }

    public void sendOTP(String email) {
        var response = authenticationService.sendOTP(email);

        if (response == null)
            return;

        if (response.ok()) {
            UserSession.setSessionId("temp session");
            UserSession.setEmail(email);
            DialogUtils.closeOpenDialogs();
            UI.getCurrent().navigate(OtpView.class, new RouteParameters("key", "changePwd"));
            Log.info(UserSession.class, email + " generated an OTP for password change.");
        }
        else
            Log.warn(UserSession.class, email + " failed to generate an OTP for password change.");
    }
}
