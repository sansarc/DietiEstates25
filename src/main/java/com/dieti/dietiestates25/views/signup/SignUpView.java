package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.views.upload.utils.Response;
import com.dieti.dietiestates25.services.AuthenticationService;
import com.dieti.dietiestates25.views.ui_components.CustomDivCard;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.views.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.ui_components.ThirdPartyLoginButton;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.VaadinApplicationConfiguration;

@Route("signup")
@PageTitle("Sign Up")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {

    H3 title = new H3("Sign Up");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm password");
    ThirdPartyLoginButton googleButton = new ThirdPartyLoginButton("Google", "50%", "/images/google_logo.png", "/oauth2/authorization/google");
    ThirdPartyLoginButton linkedinButton = new ThirdPartyLoginButton("Linkedin", "50%", "/images/linkedin_logo.png", "");
    ThirdPartyLoginButton facebookButton = new ThirdPartyLoginButton("Facebook", "50%", "/images/facebook_logo.png", "");
    CustomDivCard signupDiv = new CustomDivCard("600px", "auto");

    private final AuthenticationService authenticationService;

    SignUpView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        configureLayout();
        configureComponents();

        add(new DietiEstatesLogo(), signupDiv);
    }

    private void configureComponents() {
        signupDiv.add(
                title,
                new TextWithLink("Already have an account?", new RouterLink("Log in", LoginView.class)),
                new TextWithLink("Looking to register your agency? Do it ", new RouterLink("here", RegisterAgencyView.class)),
                createFormLayout(),
                createSignUpButton(),
                createDisclaimer(),
                new Hr(),
                googleButton,
                linkedinButton,
                facebookButton
        );
    }

    private void configureLayout() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title, googleButton, linkedinButton, facebookButton);

        setSpacing(false);
        // setPadding(false);
    }

    private Button createSignUpButton() {
        Button signUpButton = new Button("Sign Up");
        signUpButton.setWidth("50%");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUpButton.addClickShortcut(Key.ENTER);
        setAlignSelf(Alignment.CENTER, signUpButton);

        signUpButton.addClickListener(event -> {
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("Fill all the required fields.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            else if (!password.getValue().equals(confirmPassword.getValue())) {
                password.setInvalid(true);
                confirmPassword.setInvalid(true);
                confirmPassword.setErrorMessage("Passwords do not match.");
            }
            else signup();
        });

        return signUpButton;
    }

    private void signup() {
        Response signed = authenticationService.createUser(firstName.getValue(), lastName.getValue(), email.getValue(), password.getValue());
        if (signed.getStatusCode() == Constants.Codes.CREATED) {
            VaadinSession session = VaadinSession.getCurrent();
            session.setAttribute("email", email.getValue());
            UI.getCurrent().navigate(OtpView.class);
        }
        else
            Notification.show(signed.getStatusMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private Paragraph createDisclaimer() {
        Paragraph disclaimer = new Paragraph();
        setAlignSelf(Alignment.CENTER, disclaimer);
        disclaimer.getStyle()
                .setFontSize("12px")
                .set("color", "var(--lumo-secondary-text-color)");

        Span preTerms = new Span("By selecting Sign Up, you agree to our ");
        Anchor terms = new Anchor("#", "Terms");
        terms.getStyle()
                .setFontSize("12px")
                .set("color", "var(--lumo-primary-color)");

        Span andText = new Span(" and have read and acknowledge our ");
        Anchor privacy = new Anchor("#", "Privacy Policy");
        privacy.getStyle()
                .setFontSize("12px")
                .set("color", "var(--lumo-primary-color)");
        Span dot = new Span(".");

        disclaimer.add(preTerms, terms, andText, privacy, dot);
        return disclaimer;
    }

    private FormLayout createFormLayout() {
        firstName.setRequired(true);
        lastName.setRequired(true);
        email.setRequired(true);
        password.setRequired(true);
        confirmPassword.setRequired(true);
        email.setErrorMessage("Please enter a valid email");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, password, confirmPassword);
        formLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setColspan(email, 2);

        return formLayout;
    }
}
