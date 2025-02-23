package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.home.LandingPage;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.views.upload.UploadView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    HorizontalLayout header;
    HorizontalLayout navigationBar;
    DietiEstatesLogo logo;
    HorizontalLayout variablePartNavigationBar;

    public MainLayout() {
        configureComponents();
        configureLayout();
        addToNavbar(header);
    }

    private void configureComponents() {

        createLogo();
        createNavigationBar();
        createVariablePartNavigationBar();
        header = new HorizontalLayout(logo, navigationBar, variablePartNavigationBar);
        header.expand(navigationBar);

    }

    private void createVariablePartNavigationBar() {
        variablePartNavigationBar = new HorizontalLayout();

        // not sure if this is the best way
        if (VaadinSession.getCurrent().getAttribute("session_id") == null) {
            var loginButton = createButton("Login", LoginView.class);
            var signupButton = createButton("Sign up", SignUpView.class);
            signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            variablePartNavigationBar.add(loginButton, signupButton);
        }
        else {
            var logoutButton = createButton("Logout", LandingPage.class);
            variablePartNavigationBar.add(logoutButton);
        }
    }

    private void createLogo() {
        logo = new DietiEstatesLogo();
        logo.addClickListener(event -> UI.getCurrent().navigate(HomeView.class));
        logo.getStyle()
                .setCursor("pointer")
                .setMarginRight("-5%")
                .setPaddingTop("10px")
                .setPaddingLeft("10px");
    }

    private Button createButton(String label, Class<? extends Component> linkClass) {
        var button = new Button(label, event -> UI.getCurrent().navigate(linkClass));
        button.getStyle().setMarginLeft("var(--lumo-space-m)");

        return button;
    }

    private void createNavigationBar() {
        navigationBar = new HorizontalLayout();
        navigationBar.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navigationBar.addClassNames(
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL,
                LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL
        );
        navigationBar.add(
                createLink("Don't", RegisterAgencyView.class),
                createLink("Know", UploadView.class),
                createLink("About", RegisterAgencyView.class)
        );
    }

    private RouterLink createLink(String label, Class<? extends Component> linkClass) {
        RouterLink link = new RouterLink(label, linkClass);
        link.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.FontWeight.EXTRABOLD
        );
        return link;
    }

    private void configureLayout() {
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().setPaddingRight("var(--lumo-space-m)");
    }
}
