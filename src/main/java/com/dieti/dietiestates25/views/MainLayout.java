package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.ui_components.UserMenu;
import com.dieti.dietiestates25.constants.Constants.LumoSpacing;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.utils.ThemeManager;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.search.SearchView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.dieti.dietiestates25.views.upload.UploadView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;

import java.util.List;
import java.util.Map;

public class MainLayout extends AppLayout implements AfterNavigationObserver {

    HorizontalLayout header;
    HorizontalLayout navigationBar;
    DietiEstatesLogo logo;
    HorizontalLayout variablePartNavigationBar;
    Select<String> themeMode;
    Button searchButton;

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

        header = new HorizontalLayout();

        var leftSection = new HorizontalLayout(navigationBar);
        leftSection.setWidth("33%");
        leftSection.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        var centerSection = new HorizontalLayout(logo);
        centerSection.setWidth("33%");
        centerSection.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        var rightSection = new HorizontalLayout(variablePartNavigationBar);
        rightSection.setWidth("33%");
        rightSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        header.add(leftSection, centerSection, rightSection);
    }


    private void refreshVariablePartNavigationBar() {
        variablePartNavigationBar.removeAll();
        String spacing;

        if (UserSession.isUserLoggedIn()) {
            if (UserSession.isManagerOrAgent()) {
                var upload = new Button("Upload", VaadinIcon.HOME.create(), event -> UI.getCurrent().navigate(UploadView.class));
                upload.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                upload.setWidth("140px");
                variablePartNavigationBar.add(upload);
            }

            variablePartNavigationBar.add(new UserMenu());
            variablePartNavigationBar.getStyle().setMarginLeft("30px");
            spacing = LumoSpacing.M;
        }
        else {
            var loginButton = new Button("Login", event -> UI.getCurrent().navigate(LoginView.class));
            var signupButton = new Button("Sign up", event -> UI.getCurrent().navigate(SignUpView.class));
            signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            variablePartNavigationBar.add(loginButton, signupButton);
            spacing = LumoSpacing.XS;
        }

        createThemeModeMenu();

        variablePartNavigationBar.getChildren()   // more spacing between components
                .forEach(component ->
                        component.getStyle().setMarginRight(spacing).setMarginLeft(spacing));

        variablePartNavigationBar.add(themeMode);
        variablePartNavigationBar.setAlignSelf(FlexComponent.Alignment.CENTER, themeMode);
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

    private void createLogo() {
        logo = new DietiEstatesLogo(true);
        logo.getStyle()
                .setMarginRight("-5%")
                .setPaddingTop("10px")
                .setPaddingLeft("10px");
    }

    private void createNavigationBar() {
        navigationBar = new HorizontalLayout();
        navigationBar.setAlignItems(FlexComponent.Alignment.CENTER);
        navigationBar.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navigationBar.addClassNames(
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL,
                LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL
        );

        var searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchField.addThemeVariants();
        searchField.setWidth("220px");
        searchField.getStyle().setMarginRight("-15px");

        searchButton = getSearchButton(searchField);
        searchButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        var advancedSearch = new RouterLink("Advanced Search", SearchView.class);
        advancedSearch.getStyle().setFontSize("13px").setMarginLeft("-2px");

        var searchGroup = new HorizontalLayout(searchField, searchButton, advancedSearch);
        searchGroup.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        searchGroup.setPadding(false);
        searchGroup.setSpacing(true);
        searchGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        searchGroup.getStyle().setMarginLeft("15px");

        navigationBar.add(searchGroup);
    }

    public static Button getSearchButton(TextField searchField) {
        var searchButton = new Button(VaadinIcon.SEARCH.create());
        searchButton.setDisableOnClick(true);

        searchButton.addClickListener(event -> {
            if (!searchField.getValue().isBlank()) {
                UI.getCurrent().navigate(SearchView.class, new QueryParameters(Map.of("locationAny", List.of(searchField.getValue()))));
            }
            searchButton.setEnabled(true);
            searchField.clear();
        });

        return searchButton;
    }

    private void configureLayout() {
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().setPaddingRight("var(--lumo-space-m)");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {    // refreshes MainLayout "variable part" after user logs in, the best alternative to full page reload
        UI.getCurrent().getChildren()
                .filter(MainLayout.class::isInstance)
                .map(MainLayout.class::cast)
                .findFirst()
                .ifPresent(MainLayout::refreshVariablePartNavigationBar);
    }
}
