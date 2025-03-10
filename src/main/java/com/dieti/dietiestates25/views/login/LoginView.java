package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.annotations.ForwardLoggedUser;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.ui_components.ThirdPartyLoginButton;
import com.dieti.dietiestates25.views.login.specific_components.ForgotPasswordDialog;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.dieti.dietiestates25.views.signup.SignUpView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;

@ForwardLoggedUser()
@PageTitle("Login")
@Route("login")
public class LoginView extends VerticalLayout {

    H3 title = new H3("Login");
    EmailField emailField = new EmailField("Email");
    PasswordField passwordField = new PasswordField("Password");
    Button loginButton = createLoginButton();
    Anchor forgotPasswordLink = new Anchor("#", "Forgot Password?");
    DivContainer loginDiv = new DivContainer("400px", "auto");
    ThirdPartyLoginButton googleButton = new ThirdPartyLoginButton("Google", "75%", "/images/google_logo.png", "/oauth2/authorization/google");
    ThirdPartyLoginButton linkedinButton = new ThirdPartyLoginButton("Linkedin", "75%", "/images/linkedin_logo.png", "");
    ThirdPartyLoginButton facebookButton = new ThirdPartyLoginButton("Facebook", "75%", "/images/facebook_logo.png", "");

    private final AuthenticationHandler authHandler = new AuthenticationHandler();

    public LoginView() {
        configureLayout();
        configureComponents();
        add(new DietiEstatesLogo(true), loginDiv, createFooter());
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
        footer.add(new Span("Made with ♥️ by DietiEstates Team."));
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
            else
                authHandler.login(emailField.getValue(), passwordField.getValue());
        });

        return button;
    }
}
