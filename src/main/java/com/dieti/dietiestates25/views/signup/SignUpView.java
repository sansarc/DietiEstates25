package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.annotations.forward_logged_user.ForwardLoggedUser;
import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.*;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.registerAgency.RegisterAgencyView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@ForwardLoggedUser
@Route(value = "signup", layout = MainLayout.class)
@PageTitle("Sign Up")
public class SignUpView extends VerticalLayout {

        H3 title;
        Form form;
        TextField firstName, lastName;
        EmailField email;
        PasswordField password, confirmPassword;
        Paragraph disclaimer;
        Button signup;
        ThirdPartyLoginButton googleButton, linkedinButton, facebookButton;

        private final AuthenticationHandler authHandler = new AuthenticationHandler();

        SignUpView() {
            configureComponents();
            configureLayout();
        }

        private void configureComponents() {
            title = new H3("Sign Up");
            var signupDiv = new DivContainer("600px", "auto");

            createForm();
            createSignUpButton();
            createDisclaimer();
            googleButton = new ThirdPartyLoginButton("Google", "50%", "/images/google_logo.png", "/oauth2/authorization/google");
            linkedinButton = new ThirdPartyLoginButton("Linkedin", "50%", "/images/linkedin_logo.png", "");
            facebookButton = new ThirdPartyLoginButton("Facebook", "50%", "/images/facebook_logo.png", "");

            signupDiv.add(
                    title,
                    new TextWithLink("Already have an account?", new RouterLink("Log in", LoginView.class), 14),
                    new TextWithLink("Looking to register your agency? Do it ", new RouterLink("here", RegisterAgencyView.class), 12),
                    form,
                    signup,
                    disclaimer,
                    new Hr(),
                    googleButton,
                    linkedinButton,
                    facebookButton
            );

            add(new DietiEstatesLogo(true), signupDiv);
        }

        private void configureLayout() {
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.START);
            setAlignSelf(Alignment.CENTER, title, googleButton, linkedinButton, facebookButton, disclaimer, signup);

            setSpacing(false);
        }

        private void createSignUpButton() {
            signup = new Button("Sign Up");
            signup.setWidth("50%");
            signup.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            signup.addClickShortcut(Key.ENTER);

            signup.addClickListener(event -> {
                if (form.areRequiredFieldsValid() && password.getValue().equals(confirmPassword.getValue()))
                    authHandler.createUser(firstName.getValue(), lastName.getValue(), email.getValue(), password.getValue());
            });

        }

        private void createDisclaimer() {
            disclaimer = new Paragraph();
            disclaimer.getStyle()
                    .setFontSize("12px")
                    .setColor(Constants.Colors.SECONDARY_GRAY);

            Span preTerms = new Span("By selecting Sign Up, you agree to our ");
            Anchor terms = new Anchor("#", "Terms");
            terms.getStyle()
                    .setFontSize("12px")
                    .setColor(Constants.Colors.PRIMARY_BLUE);

            Span andText = new Span(" and have read and acknowledge our ");
            Anchor privacy = new Anchor("#", "Privacy Policy");
            privacy.getStyle()
                    .setFontSize("12px")
                    .setColor(Constants.Colors.PRIMARY_BLUE);
            Span dot = new Span(".");

            disclaimer.add(preTerms, terms, andText, privacy, dot);
        }

        private void createForm() {
            form = new Form();

            firstName = new TextField("First name");
            lastName = new TextField("Last name");
            email = new EmailField("Email");
            password = new PasswordField("Password");
            confirmPassword = new PasswordField("Confirm password");

            email.setErrorMessage("Please enter a valid email");

            form.add(firstName, lastName, email, password, confirmPassword);
            form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
            );
            form.setColspan(email, 2);

            form.setRequiredTrue(firstName, lastName, email, password, confirmPassword);
        }
}
