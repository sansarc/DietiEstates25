package com.dieti.dietiestates25.views.login;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;

public class ForgotPasswordDialog extends Dialog {

    VerticalLayout layout;
    EmailField emailField;
    Span subtitle;
    VerticalLayout emailLayout;

    private final AuthenticationHandler authenticationHandler = new AuthenticationHandler();

    public ForgotPasswordDialog() {
        configureComponents();
        configureLayout();
    }

    private void configureLayout() {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        addDialogCloseActionListener(event -> close());

        layout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void configureComponents() {
        layout = new VerticalLayout();
        var title = new H3("Forgot Password?");

        createSubtitle();
        createEmailLayout();
        
        var sendButton = new Button("Send", event -> {
            if (emailField.isInvalid())
                emailField.setInvalid(true);
            else
                authenticationHandler.sendOTP(emailField.getValue());
        });
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.addClickShortcut(Key.ENTER);
        getFooter().add(new Button("Close", event -> close()), sendButton);

        layout.add(title, subtitle, emailLayout);
        add(layout);
    }

    private void createSubtitle() {
        subtitle = new Span("No worries, we'll send you reset instructions.");
        subtitle.getStyle()
                .setFontSize("14px")
                .setColor(Constants.Colors.SECONDARY_GRAY);
    }

    private void createEmailLayout() {
        emailField = new EmailField("Email");
        emailField.setErrorMessage("Please enter a valid email address.");
        emailField.setWidthFull();

        var helperText = new Span("Make sure to use the email address linked to your account.");
        helperText.getStyle()
                .setFontSize("12px")
                .setColor("var(--lumo-tertiary-text-color)");

        emailLayout = new VerticalLayout(emailField, helperText);
        emailLayout.setSpacing(false);
        emailLayout.setPadding(false);
    }
}
