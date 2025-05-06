package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.annotations.forward_logged_user.ForwardLoggedUser;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.ui_components.ThirdPartyLoginButton;
import com.dieti.dietiestates25.views.MainLayout;
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

import java.util.List;
import java.util.Map;

@ForwardLoggedUser
@PageTitle("Login")
@Route(value = "login", layout = MainLayout.class)
public class LoginView extends VerticalLayout implements HasUrlParameter<String> {

    public static final String TMP_PWD_LITERAL = "tmpPwd";
    H3 title = new H3("Login");
    EmailField emailField = new EmailField("Email");
    PasswordField passwordField = new PasswordField("Password");
    Button loginButton = createLoginButton();
    Anchor forgotPasswordLink = new Anchor("#", "Forgot Password?");
    DivContainer loginDiv = new DivContainer("400px", "auto");
    ThirdPartyLoginButton googleButton = new ThirdPartyLoginButton("Google", "75%", "/images/google_logo.png", "/oauth2/authorization/google");
    ThirdPartyLoginButton linkedinButton = new ThirdPartyLoginButton("Linkedin", "75%", "/images/linkedin_logo.png", "");
    ThirdPartyLoginButton facebookButton = new ThirdPartyLoginButton("Facebook", "75%", "/images/facebook_logo.png", "");

    transient AuthenticationHandler authHandler = new AuthenticationHandler();

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        var queryParameters = event.getLocation().getQueryParameters();
        Map<String, List<String>> parameters = queryParameters.getParameters();

        if (parameters.containsKey(TMP_PWD_LITERAL) && !parameters.get(TMP_PWD_LITERAL).isEmpty()) {
            var tmpPwd = parameters.get(TMP_PWD_LITERAL).getFirst();
            configureComponents(tmpPwd);
        }
        else
            configureComponents(null);
    }

    public LoginView() {
        configureLayout();
        add(loginDiv, createFooter());
    }

    private void configureComponents(String tmpPwd) {
        loginFormSetUp(tmpPwd);

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

    private void loginFormSetUp(String tmpPwd) {
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        emailField.setErrorMessage("Please enter a valid email address");

        passwordField.setPrefixComponent(new Icon(VaadinIcon.KEY));
        passwordField.setErrorMessage("Please enter a password");
        if (tmpPwd != null)
            passwordField.setValue(tmpPwd);

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
