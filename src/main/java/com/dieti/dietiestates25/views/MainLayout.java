package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.views.profile.Profile;
import com.dieti.dietiestates25.views.agency_dashboard.AgencyDashboardView;
import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.ui_components.NotificationBell;
import com.dieti.dietiestates25.utils.ThemeManager;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.dieti.dietiestates25.views.upload.UploadView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;

public class MainLayout extends AppLayout implements AfterNavigationObserver {

    HorizontalLayout header;
    HorizontalLayout navigationBar;
    DietiEstatesLogo logo;
    HorizontalLayout variablePartNavigationBar;
    Avatar avatar;
    Select<String> themeMode;

    private final AuthenticationHandler authenticationHandler = new AuthenticationHandler();

    public MainLayout() {
        configureComponents();
        configureLayout();
        addToNavbar(header);
    }

    private void configureComponents() {

        createLogo();
        createNavigationBar();
        variablePartNavigationBar = new HorizontalLayout();
        refreshVariablePartNavigationBar();
        header = new HorizontalLayout(logo, navigationBar, variablePartNavigationBar);
        header.expand(navigationBar);

    }

    private void refreshVariablePartNavigationBar() {
        variablePartNavigationBar.removeAll();

        if (UserSession.isUserLoggedIn()) {
            createAvatarBadge();
            var notificationButton = new NotificationBell();
            variablePartNavigationBar.add(notificationButton, avatar);
        }
        else {
            var loginButton = new Button("Login", event -> UI.getCurrent().navigate(LoginView.class));
            var signupButton = new Button("Sign up", event -> UI.getCurrent().navigate(SignUpView.class));
            signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            variablePartNavigationBar.add(loginButton, signupButton);
        }

        createThemeModeMenu();

        variablePartNavigationBar.getChildren()
                .forEach(component ->
                        component.getStyle().setMarginRight("var(--lumo-space-xs)").setMarginLeft("var(--lumo-space-xs)"));

        variablePartNavigationBar.add(themeMode);
        variablePartNavigationBar.setAlignSelf(HorizontalLayout.Alignment.CENTER, themeMode);
    }

    private void createThemeModeMenu() {
        themeMode = new Select<>();
        themeMode.setWidth("70px");
        themeMode.setItems("light", "dark");


        themeMode.setRenderer(new ComponentRenderer<>(mode -> {
            Icon icon;
            if ("light".equals(mode))
                icon = VaadinIcon.SUN_O.create();
            else
                icon = VaadinIcon.MOON_O.create();
            String iconSize = "16px";
            icon.getStyle().setWidth(iconSize).setHeight(iconSize);
            return icon;
        }));

        themeMode.addValueChangeListener(event -> {
            ThemeManager.applyThemeChange(event.getValue(), themeMode);
            ThemeChangeNotifier.notifyThemeChange(ThemeManager.isDarkThemeOn());
        });

        ThemeManager.initializeTheme(themeMode);
    }

    private void createAvatarBadge() {
        String name = UserSession.getFirstName() + " " + UserSession.getLastName();

        avatar = new Avatar(name);
        avatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
        avatar.getStyle()
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE)
                .setCursor("pointer");

        var userMenu = new ContextMenu();
        userMenu.setTarget(avatar);
        userMenu.setOpenOnClick(true);

        if (UserSession.isManagerOrAgent()) {
            var agency = new Span(UserSession.getAgency());
            agency.getStyle().setFontWeight(Style.FontWeight.BOLD);
            userMenu.addItem(agency)
                    .addClickListener(event -> UI.getCurrent().navigate(AgencyDashboardView.class));
        }

        userMenu.addItem(new Span("Your Profile"))
                .addClickListener(event -> UI.getCurrent().navigate(Profile.class));

        var logout = new Span("Logout");
        logout.getStyle().setColor("red");
        userMenu.addItem(logout)
                .addClickListener(event -> {
                    authenticationHandler.logout(UserSession.getSessionId());
                    UserSession.logout(false);
                });

        for (Component item : userMenu.getItems()) {
            item.getStyle().setCursor("pointer");
        }
    }

    private void createLogo() {
        logo = new DietiEstatesLogo(true);
        logo.getStyle()
                .setMarginRight("-5%")
                .setPaddingTop("10px")
                .setPaddingLeft("10px");
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

    @Override
    public void afterNavigation(AfterNavigationEvent event) {    // refreshes MainLayout "variable part" after user logs in, the best alternative to full page reload
        UI.getCurrent().getChildren()
                .filter(component -> component instanceof MainLayout)
                .map(component -> (MainLayout) component)
                .findFirst()
                .ifPresent(MainLayout::refreshVariablePartNavigationBar);
    }
}
