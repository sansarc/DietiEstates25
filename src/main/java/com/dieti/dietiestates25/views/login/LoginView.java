package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.dto.SessionResponse;
import com.dieti.dietiestates25.services.AuthenticationService;
import com.dieti.dietiestates25.views.ui_components.CustomDivCard;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.views.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.ui_components.ThirdPartyLoginButton;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route("login")
public class LoginView extends VerticalLayout {

    H3 title = new H3("Login");
    EmailField emailField = new EmailField("Email");
    PasswordField passwordField = new PasswordField("Password");
    Button loginButton = createLoginButton();
    Anchor forgotPasswordLink = new Anchor("#", "Forgot Password?");
    CustomDivCard loginDiv = new CustomDivCard("400px", "auto");
    ThirdPartyLoginButton googleButton = new ThirdPartyLoginButton("Google", "75%", "/images/google_logo.png", "/oauth2/authorization/google");
    ThirdPartyLoginButton linkedinButton = new ThirdPartyLoginButton("Linkedin", "75%", "/images/linkedin_logo.png", "");
    ThirdPartyLoginButton facebookButton = new ThirdPartyLoginButton("Facebook", "75%", "/images/facebook_logo.png", "");

    private final AuthenticationService authenticationService;

    public LoginView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        configureLayout();
        configureComponents();

        add(new DietiEstatesLogo(), loginDiv, createFooter());
    }

    private void configureComponents() {
        loginFormSetUp();
        loginDiv.add(
                title,
                emailField,
                passwordField,
                forgotPasswordLink,
                loginButton,
                new Hr(),
                googleButton,
                linkedinButton,
                facebookButton,
                new Hr(),
                new TextWithLink("Don't have an account yet?", new RouterLink("Sign up", SignUpView.class)),
                new TextWithLink("Want to register your agency? Do it", new RouterLink("here", RegisterAgencyView.class))
        );
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title, loginButton, googleButton, linkedinButton, facebookButton);
        setAlignSelf(Alignment.START, forgotPasswordLink);
    }

    private Footer createFooter() {
        var footer = new Footer();
        footer.add(new Span("Made with ♥\uFE0F by DietiEstates Team."));
        footer.getStyle()
                .set("font-size", "12px")
                .set("color", "var<(--lumo-tertiary-text-color)");

        return footer;
    }

    private void loginFormSetUp() {
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        emailField.setErrorMessage("Please enter a valid email address");
        passwordField.setPrefixComponent(new Icon(VaadinIcon.KEY));
        passwordField.setErrorMessage("Please enter a password");
        forgotPasswordLink.getElement().getStyle().set("font-size", "12px");
        forgotPasswordLink.getElement().addEventListener("click", event ->
                new ForgotPasswordDialog().open()
        ).addEventData("event.preventDefault()");
    }

    private Button createLoginButton() {
        var button = new Button("Login");

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setPrefixComponent(new Icon(VaadinIcon.SIGN_IN));
        button.addClickShortcut(Key.ENTER);
        button.setWidth("75%");

        button.addClickListener(event -> {
            if (passwordField.isEmpty() || emailField.isEmpty()) {
                emailField.setInvalid(true);
                passwordField.setInvalid(true);
            }
            else login();
        });

        return button;
    }

    private void login() {
        SessionResponse authenticated = authenticationService.authenticate(emailField.getValue(), passwordField.getValue());
        if (authenticated.ok()) {
            Notification.show("Welcome Back!", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            VaadinSession.getCurrent().getSession().setAttribute("session_id", authenticated.getSessionId());
            UI.getCurrent().navigate(HomeView.class);
        }
        else
            Notification.show(authenticated.getStatusMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
