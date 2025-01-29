package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.services.AuthenticationService;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@AnonymousAllowed
@PageTitle("Login")
@Route("login")
public class LoginView extends VerticalLayout {

    private final Image logo = new Image("/images/logo_cropped.png", "dietiestates_logo");
    private final EmailField emailField = new EmailField("Email");
    private final PasswordField passwordField = new PasswordField("Password");
    private final Button loginButton = new Button("Login");
    private final Anchor forgotPasswordLink = new Anchor("#", "Forgot Password?");
    private final Footer footer = new Footer();
    private final Div loginDiv = new Div();

    private final AuthenticationService authenticationService;

    public LoginView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        H4 title = new H4("Log in DietiEstates");
        setAlignSelf(Alignment.CENTER, title);

        logoSetUp();

        loginFormSetUp();
        loginButtonSetUp();

        loginDivStyling();
        loginDiv.add(
                title,
                emailField,
                passwordField,
                forgotPasswordLink,
                loginButton,
                new Hr(),
                get3rdPartyLoginButton("Google", "/images/google_logo.png", "/oauth2/authorization/google"),
                get3rdPartyLoginButton("Linkedin", "/images/linkedin_logo.png", ""),
                get3rdPartyLoginButton("Facebook", "/images/facebook_logo.png", ""),
                new Hr(),
                getTextWithLink(new Span("Don't have an account yet?"), new RouterLink("Sign up", SignUpView.class)),
                getTextWithLink(new Span("Want to register your agency? Do it"), new RouterLink("here", SignUpView.class))
        );

        footerStyling();
        add(logo, loginDiv, footer);
    }

    private void footerStyling() {
        footer.add(new Span("Made with ♥\uFE0F by DietiEstates Team."));
        footer.getStyle()
                .set("font-size", "12px")
                .set("color", "var(--lumo-tertiary-text-color)");
    }

    private HorizontalLayout getTextWithLink(Span text, RouterLink link) {
        HorizontalLayout layout = new HorizontalLayout(text, link, new Span("."));
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.getStyle().set("font-size", "14px");
        link.getStyle().set("margin-left", "4px");
        layout.setSpacing(false);
        return layout;
    }

    private Button get3rdPartyLoginButton(String label, String iconPath, String url) {
        var button = new Button("Login with " + label);
        button.setWidth("75%");
        setAlignSelf(Alignment.CENTER, button);

        Image logo = new Image(iconPath, "logo");
        logo.setHeight("16px");
        logo.setWidth("16px");
        button.setIcon(logo);

        button.addClickListener(event -> UI.getCurrent().getPage().setLocation(url));

        return button;
    }

    private void loginFormSetUp() {
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        emailField.setErrorMessage("Please enter a valid email address");
        passwordField.setPrefixComponent(new Icon(VaadinIcon.KEY));
        passwordField.setErrorMessage("Please enter a password");
        setAlignSelf(Alignment.START, forgotPasswordLink);
        forgotPasswordLink.getElement().getStyle().set("font-size", "12px");
        forgotPasswordLink.getElement().addEventListener("click", event ->
                new ForgotPasswordDialog().open()
        ).addEventData("event.preventDefault()");
    }

    private void logoSetUp() {
        logo.setWidth("100px");
        logo.setHeight("auto");
        logo.getStyle().set("margin-bottom", "var(--lumo-space-s)");
    }

    private void loginButtonSetUp() {
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setAlignSelf(Alignment.CENTER, loginButton);
        loginButton.setPrefixComponent(new Icon(VaadinIcon.SIGN_IN));
        loginButton.addClickShortcut(Key.ENTER);
        loginButton.setWidth("75%");

        loginButton.addClickListener(event -> {
            if (passwordField.isEmpty() || emailField.isEmpty()) {
                emailField.setInvalid(true);
                passwordField.setInvalid(true);
            }
            else login();
        });
    }

    private void login() {
        boolean authenticated = authenticationService.authenticate(emailField.getValue(), passwordField.getValue());
        if (authenticated) {
            Notification.show("Login successful!", 5000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(HomeView.class);
        }
        else
            Notification.show("Invalid credentials.", 5000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void loginDivStyling() {
        loginDiv.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE
        );
        loginDiv.setWidth("400px");
        loginDiv.getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "var(--lumo-space-xs)");

    }
}
