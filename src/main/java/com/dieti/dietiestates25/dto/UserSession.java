package com.dieti.dietiestates25.dto;

import com.vaadin.flow.server.VaadinSession;

public class UserSession {
    public UserSession(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
        setAgency(user.getAgency());
        setRole();
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

    public void setRole() {
        VaadinSession.getCurrent().setAttribute("role",
                getAgency() != null ? "A" : "U"
        );
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

    public String getRole() {
        return (String) VaadinSession.getCurrent().getAttribute("role");
    }

}
