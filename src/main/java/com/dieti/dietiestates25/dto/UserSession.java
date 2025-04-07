package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.services.logging.Log;
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

        if (user.getAgency() != null)
            setAgency(user.getAgency());

        if (user.getRole() != null)
            setRole(user.getRole());
    }

    public static void setSessionId(String sessionId) {
        VaadinSession.getCurrent().setAttribute("sessionId", sessionId);
    }

    public void setConfirmed(boolean confirmed) {
        VaadinSession.getCurrent().setAttribute("confirmed", confirmed);
    }

    public static void setPwd(String pwd) {
        VaadinSession.getCurrent().setAttribute("pwd", pwd);
    }

    public void setFirstName(String firstName) {
        VaadinSession.getCurrent().setAttribute("firstName", firstName);
    }

    public void setLastName(String lastName) {
        VaadinSession.getCurrent().setAttribute("lastName", lastName);
    }

    public void setEmail(String email) {
        VaadinSession.getCurrent().setAttribute("email", email);
    }

    public void setAgency(String agency) {
        VaadinSession.getCurrent().setAttribute("agency", agency);
    }

    public void setRole(String role) {
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

    public static void clearSession() {

        VaadinSession currentSession = VaadinSession.getCurrent();
        try {
            if (currentSession != null) {
                currentSession.setAttribute("firstName", null);
                currentSession.setAttribute("lastName", null);
                currentSession.setAttribute("email", null);
                currentSession.setAttribute("agency", null);
                currentSession.setAttribute("role", null);
                currentSession.setAttribute("pwd", null);
                currentSession.setAttribute("confirmed", null);
                currentSession.setAttribute("sessionId", null);
            }
        } catch (NullPointerException npe) {
            Log.warn(UserSession.class, "Session is already cleared: " + npe.getMessage());
        }
    }
}
