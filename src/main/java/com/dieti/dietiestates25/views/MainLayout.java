package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.dieti.dietiestates25.views.upload.UploadView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;

public class MainLayout extends AppLayout {

    HorizontalLayout header;
    HorizontalLayout navigationBar;
    DietiEstatesLogo logo;
    HorizontalLayout variablePartNavigationBar;
    Avatar avatar;
    Select<String> themeMode;


    public MainLayout() {
        configureComponents();
        configureLayout();
        addToNavbar(header);
    }

    private void checkThemeSettings() {
        Boolean darkTheme = (Boolean) VaadinSession.getCurrent().getAttribute("darkTheme");
        setTheme(darkTheme);
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
        if (VaadinSession.getCurrent().getAttribute("session_id") != null) {
            createAvatarBadge();
            variablePartNavigationBar.add(avatar);
        }
        else {
            var loginButton = createButton("Login", LoginView.class);
            var signupButton = createButton("Sign up", SignUpView.class);
            signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            variablePartNavigationBar.add(loginButton, signupButton);
        }

        createThemeModeMenu();

        variablePartNavigationBar.add(themeMode);
        variablePartNavigationBar.setAlignSelf(HorizontalLayout.Alignment.CENTER, themeMode);
    }

    private void createThemeModeMenu() {
        themeMode = new Select<>();
        themeMode.setWidth("70px");
        themeMode.setItems("light", "dark");


        themeMode.setRenderer(new ComponentRenderer<>(mode -> {
            Icon icon;
            if ("light".equals(mode)) {
                icon = VaadinIcon.SUN_O.create();
            } else {
                icon = VaadinIcon.MOON_O.create();
            }
            String iconSize = "16px";
            icon.getStyle().setWidth(iconSize).setHeight(iconSize);
            return icon;
        }));

        themeMode.addValueChangeListener(event -> {
            boolean isDarkThemeOn = "dark".equals(event.getValue());
            VaadinSession.getCurrent().setAttribute("darkTheme", isDarkThemeOn);
            setTheme(isDarkThemeOn);
            ThemeChangeNotifier.notifyThemeChange(isDarkThemeOn);
        });

        if (VaadinSession.getCurrent().getAttribute("darkTheme") == null)
            setTheme(false);
        else
            checkThemeSettings();
    }

    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";
        themeMode.setValue(dark ? "dark" : "light");
        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }


    private void createAvatarBadge() {
        String email = (String) VaadinSession.getCurrent().getAttribute("email");
        String username = email.replace("@gmail.com", "");

        avatar = new Avatar(username);
        avatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
        avatar.setAbbreviation(username.substring(0, 1).toUpperCase());
        avatar.getStyle()
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE)
                .setCursor("pointer");

        var userMenu = new ContextMenu();
        userMenu.setTarget(avatar);
        userMenu.setOpenOnClick(true);
        var icon = LumoIcon.COG.create();

        var preferences = new HorizontalLayout(icon, new Text("Preferences"));
        preferences.setAlignItems(FlexComponent.Alignment.START);
        preferences.setAlignSelf(FlexComponent.Alignment.BASELINE, icon);
        preferences.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        preferences.setSpacing(false);

        userMenu.addItem(preferences);
        userMenu.addItem("Logout").getStyle().setColor("red");
        for (Component item : userMenu.getItems()) item.getStyle().setCursor("pointer");
    }

    private void createLogo() {
        logo = new DietiEstatesLogo(true);
        logo.getStyle()
                .setMarginRight("-5%")
                .setPaddingTop("10px")
                .setPaddingLeft("10px");
    }

    private Button createButton(String label, Class<? extends Component> linkClass) {
        return new Button(label, event -> UI.getCurrent().navigate(linkClass));
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
