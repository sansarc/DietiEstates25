package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// questa deve essere completata ovviamente

@Route("signup")
@PageTitle("Sign Up")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {

    private final H2 title = new H2("Sign Up to DietiEstates");
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm password");
    private final ComboBox<String> region = new ComboBox<>("Region");
    private final ComboBox province = new ComboBox("Province");

    private final Div signupDiv = new Div();

    SignUpView() throws IOException, JSONException {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title);

        signupDivStyling();
        signupDiv.add(
                title,
                getTextWithLink("Already have an account?", new RouterLink("Log in", LoginView.class)),
                getFormLayout(),
                getSignUpButton(),
                getDisclaimer(),
                new Hr(),
                get3rdPartyLoginButton("Google", "/images/google_logo.png", "/oauth2/authorization/google"),       // Button type
                get3rdPartyLoginButton("Linkedin", "/images/linkedin_logo.png", ""),
                get3rdPartyLoginButton("Facebook", "/images/facebook_logo.png", "")
        );
        add(signupDiv);
    }

    private Button get3rdPartyLoginButton(String label, String iconPath, String url) {
        Button button = new Button("Sign Up with " + label);
        button.setWidth("50%");
        setAlignSelf(Alignment.CENTER, button);

        Image logo = new Image(iconPath, "logo");
        logo.setHeight("16px");
        logo.setWidth("16px");
        button.setIcon(logo);

        button.addClickListener(event -> UI.getCurrent().getPage().setLocation(url));

        return button;
    }

    private Button getSignUpButton() {
        Button signUpButton = new Button("Sign Up");
        signUpButton.setWidth("50%");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setAlignSelf(Alignment.CENTER, signUpButton);

        signUpButton.addClickListener(event -> {
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("Please fill all the required fields.")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } // else signup()
        });

        return signUpButton;
    }

    private Paragraph getDisclaimer() {
        Paragraph disclaimer = new Paragraph();
        setAlignSelf(Alignment.CENTER, disclaimer);
        disclaimer.getStyle()
                .set("font-size", "12px")
                .set("color", "var(--lumo-secondary-text-color)");

        Span preTerms = new Span("By selecting Sign Up, you agree to our ");
        Anchor terms = new Anchor("#", "Terms");
        terms.getStyle()
                .set("color", "var(--lumo-primary-color)")
                .set("font-size", "12px");

        Span andText = new Span(" and have read and acknowledge our ");
        Anchor privacy = new Anchor("#", "Privacy Policy");
        privacy.getStyle()
                .set("color", "var(--lumo-primary-color)")
                .set("font-size", "12px");
        Span dot = new Span(".");

        disclaimer.add(preTerms, terms, andText, privacy, dot);
        return disclaimer;
    }

    private FormLayout getFormLayout() throws IOException, JSONException {
        firstName.setRequired(true);
        lastName.setRequired(true);
        email.setRequired(true);
        password.setRequired(true);
        confirmPassword.setRequired(true);
        email.setErrorMessage("Please enter a valid email");

        region.setItems(getRegions());

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, region, province, password, confirmPassword);
        formLayout.setResponsiveSteps(
        // Use one column by default
        new FormLayout.ResponsiveStep("0", 1),
        // Use two columns, if layout's width exceeds 500px
        new FormLayout.ResponsiveStep("500px", 2));
        // Stretch the username field over 2 columns
        formLayout.setColspan(email, 2);

        return formLayout;
    }

    private static String[] getRegions() throws IOException, JSONException {
        URL url = new URL("https://axqvoqvbfjpaamphztgd.functions.supabase.co/regioni");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            connection.disconnect();
        }

        JSONArray jsonArray = new JSONArray(response.toString());
        String[] regions = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            regions[i] = jsonArray.getString(i);
        }

        return regions;
    }

    private HorizontalLayout getTextWithLink(String text, RouterLink link) {
        HorizontalLayout layout = new HorizontalLayout(new Span(text), link);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.getStyle().set("font-size", "14px");
        link.getStyle().set("margin-left", "4px");
        layout.setSpacing(false);
        return layout;
    }

    private void signupDivStyling() {
        signupDiv.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE
        );
        signupDiv.setWidth("800px");
        signupDiv.getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "var(--lumo-space-xxs)");
    }
}
