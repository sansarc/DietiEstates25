package com.dieti.dietiestates25.services.session;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSession {

    private static final Logger logger = LoggerFactory.getLogger(UserSession.class);

    public static final String SESSION_START = "sessionStart";

    private UserSession() {}

    public static void init(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
        setRole(user.getRole() == null ? "user" : user.getRole());

        if (user.getConfirmed() != null)
            setConfirmed(user.getConfirmed());

        if (user.getPwd() != null)
            setPwd(user.getPwd());

        if (user.getAgencyName() != null) {
            setAgencyName(user.getAgencyName());
            setAgencyVAT(user.getAgencyVAT());
        }
    }

    public static String getCurrentPath() {
        return UI.getCurrent().getInternals().getActiveViewLocation().getPath();
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
        VaadinSession.getCurrent().setAttribute(SESSION_START, System.currentTimeMillis());
    }

    public static Long getSessionStart() {
        return (Long) VaadinSession.getCurrent().getAttribute(SESSION_START);
    }

    public static void setSessionStart(Long sessionStart) {
        VaadinSession.getCurrent().setAttribute(SESSION_START, sessionStart);
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

    public static void setAgencyName(String agency) {
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

    public static String getAgencyName() {
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
                getAgencyName(),
                getAgencyVAT(),
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
                setAgencyName(null);
                setRole(null);
                setSessionId(null);
                setSessionStart(null);
            }
        } catch (NullPointerException npe) {
            logger.warn("Session is already cleared: {}", npe.getMessage());
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
            logger.info("{} session expired.", email);
    }
}
