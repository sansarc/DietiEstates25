package com.dieti.dietiestates25.views.home;

import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    public HomeView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Hi, ");
        Button logoutButton = new Button("Logout", event -> logout());

        add(title, logoutButton);
    }

    private void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
        UI.getCurrent().navigate(LoginView.class);
        Notification.show("Logged out successfully", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!isUserLoggedIn())
            event.rerouteTo(LoginView.class);
    }

    private boolean isUserLoggedIn() {
        return VaadinSession.getCurrent().getAttribute("user") != null;
    }

}
