package com.dieti.dietiestates25.services.session;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.logging.Log;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class UserSession {

    public UserSession(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());

        if (user.getConfirmed() != null)
            setConfirmed(user.getConfirmed());

        if (user.getPwd() != null)
            setPwd(user.getPwd());

        if (user.getAgency() != null) {
            setAgency(user.getAgency());
            setAgencyVAT(user.getAgencyVAT());
        }

        if (user.getRole() != null)
            setRole(user.getRole());
    }

    public static void setAgencyVAT(String agencyVAT) {
        VaadinSession.getCurrent().setAttribute("agencyVAT", agencyVAT);
    }

    public static String getAgencyVAT() {
        return (String) VaadinSession.getCurrent().getAttribute("agencyVAT");
    }

    public static void setTheme(boolean mode) {
        VaadinSession.getCurrent().setAttribute("darkTheme", mode);
    }

    public static Boolean isDarkThemeOn () {
        return (Boolean) VaadinSession.getCurrent().getAttribute("darkTheme");
    }

    public static boolean isUserLoggedIn() {
        return UserSession.getSessionId() != null;
    }

    public static void setSessionId(String sessionId) {
        VaadinSession.getCurrent().setAttribute("sessionId", sessionId);
        VaadinSession.getCurrent().setAttribute("sessionStart", System.currentTimeMillis());
    }

    public static Long getSessionStart() {
        return (Long) VaadinSession.getCurrent().getAttribute("sessionStart");
    }

    public static void setSessionStart(Long sessionStart) {
        VaadinSession.getCurrent().setAttribute("sessionStart", sessionStart);
    }

    public static void setConfirmed(boolean confirmed) {
        VaadinSession.getCurrent().setAttribute("confirmed", confirmed);
    }

    public static void setPwd(String pwd) {
        VaadinSession.getCurrent().setAttribute("pwd", pwd);
    }

    public static void setFirstName(String firstName) {
        VaadinSession.getCurrent().setAttribute("firstName", firstName);
    }

    public static void setLastName(String lastName) {
        VaadinSession.getCurrent().setAttribute("lastName", lastName);
    }

    public static void setEmail(String email) {
        VaadinSession.getCurrent().setAttribute("email", email);
    }

    public static void setAgency(String agency) {
        VaadinSession.getCurrent().setAttribute("agency", agency);
    }

    public static void setRole(String role) {
        VaadinSession.getCurrent().setAttribute("role", role);
    }

    public static String getFirstName() {
        return (String) VaadinSession.getCurrent().getAttribute("firstName");
    }

    public static String getLastName() {
        return (String) VaadinSession.getCurrent().getAttribute("lastName");
    }

    public static String getEmail() {
        return (String) VaadinSession.getCurrent().getAttribute("email");
    }

    public static String getAgency() {
        return (String) VaadinSession.getCurrent().getAttribute("agency");
    }

    public static String getRole() {
        return (String) VaadinSession.getCurrent().getAttribute("role");
    }

    public static boolean isAgent() {
        var role = (String) VaadinSession.getCurrent().getAttribute("role");
        return role != null && role.equals("A");
    }

    public static boolean isManager() {
        var role = (String) VaadinSession.getCurrent().getAttribute("role");
        return role != null && role.equals("M");    }

    public static String getPwd() {
        return (String) VaadinSession.getCurrent().getAttribute("pwd");
    }

    public static boolean isConfirmed() {
        return (boolean) VaadinSession.getCurrent().getAttribute("confirmed");
    }

    public static boolean isManagerOrAgent() {
        return isManager() || isAgent();
    }

    public static String getSessionId() {
        return (String) VaadinSession.getCurrent().getAttribute("sessionId");
    }

    public static User asUser() {
        return new User(
                getFirstName(),
                getLastName(),
                getEmail(),
                null,
                getAgency(),
                getRole(),
                isConfirmed()
        );
    }


    public static void clearSession() {
        VaadinSession currentSession = VaadinSession.getCurrent();
        try {
            if (currentSession != null) {
                setFirstName(null);
                setFirstName(null);
                setEmail(null);
                setPwd(null);
                setAgency(null);
                setRole(null);
                setSessionId(null);
                setSessionStart(null);
            }
        } catch (NullPointerException npe) {
            Log.warn(UserSession.class, "Session is already cleared: " + npe.getMessage());
        }
    }

    public static void logout(boolean isSessionExpired) {
        var email = UserSession.getEmail();
        clearSession();
        var ui = UI.getCurrent();

        ui.access(() -> {
            if (isSessionExpired)
                ui.navigate(LoginView.class);
            else
                ui.navigate(HomeView.class);
            ui.getElement().executeJs("window.location.reload();");   // refresh
        });

        if (isSessionExpired)
            Log.info(UserSession.class, email + " session expired, logging out...");
        else
            Log.info(UserSession.class, email + " logged out successfully.");
    }
}
