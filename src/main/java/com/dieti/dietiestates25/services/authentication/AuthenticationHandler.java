package com.dieti.dietiestates25.services.authentication;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.dto.SessionResponse;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.signup.OtpView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

public class AuthenticationHandler {
    private final AuthenticationService authenticationService;

    public AuthenticationHandler() {
        authenticationService = new AuthenticationService();
    }

    public void confirmUser(String email, String otp) {
        Response confirmed = authenticationService.confirmUser(email, otp);
            VaadinSession.getCurrent().setAttribute("email", null);

        if (confirmed.ok()) {
            Notification.show(confirmed.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(LoginView.class);
        } else
            Notification.show(confirmed.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public void login(String email, String password) {
        try {
            SessionResponse authenticated = authenticationService.authenticate(email, password);
            if (authenticated.ok()) {
                NotificationFactory.success("Welcome Back!");
                UI.getCurrent().navigate(HomeView.class);
            } else
                NotificationFactory.error(authenticated.getMessage());

        } catch (RuntimeException e) {
            NotificationFactory.critical();
        }
    }

    public void signup(String firstName, String lastName, String email, String password) {
        try {
            Response signed = authenticationService.createUser(firstName, lastName, email, password);
            VaadinSession session = VaadinSession.getCurrent();
            if (session == null) {
                session = new VaadinSession(VaadinService.getCurrent());
                VaadinSession.setCurrent(session);
            }
            if (signed.getStatusCode() == Constants.Codes.CREATED) {
                session.setAttribute("email", email);
                UI.getCurrent().navigate(OtpView.class);
            }
            else
                NotificationFactory.error(signed.getMessage());
        } catch (RuntimeException e) {
            NotificationFactory.critical();
        }
    }
}
