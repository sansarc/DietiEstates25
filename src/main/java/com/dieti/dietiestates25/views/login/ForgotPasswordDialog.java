package com.dieti.dietiestates25.views.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;

public class ForgotPasswordDialog extends Dialog {

    private final VerticalLayout layout = new VerticalLayout();
    private final H3 title = new H3("Forgot Password?");
    private final EmailField emailField = new EmailField("Email");
    private final Span helperText = new Span("Make sure to use the email address linked to your account.");
    private final Button sendButton = createSendButton();
    private final Button cancelButton = createCancelButton();

    public ForgotPasswordDialog() {
        configureComponents();
        assembleLayout();
    }

    private void configureComponents() {
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        emailField.setWidthFull();
        emailField.setErrorMessage("Please enter a valid email address");
        helperText.getStyle()
                .set("font-size", "12px")
                .set("color", "var(--lumo-tertiary-text-color)");

        cancelButton.addClickShortcut(Key.ESCAPE);
    }

    private void assembleLayout() {
        HorizontalLayout buttonsLayout = createButtonsLayout();
        VerticalLayout emailLayout = createEmailLayout();

        layout.add(title, createSubtitle(), emailLayout, buttonsLayout);
        add(layout);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout(sendButton, cancelButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private VerticalLayout createEmailLayout() {
        VerticalLayout layout = new VerticalLayout(emailField, helperText);
        layout.setSpacing(false);
        layout.setPadding(false);
        return layout;
    }

    private Span createSubtitle() {
        Span subtitle = new Span("No worries, we'll send you reset instructions.");
        subtitle.getStyle()
                .set("font-size", "14px")
                .set("color", "var(--lumo-secondary-text-color)");
        return subtitle;
    }

    private Button createSendButton() {
        Button button = new Button("Send");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> handleSendButtonClick());
        button.addClickShortcut(Key.ENTER);
        return button;
    }

    private Button createCancelButton() {
        Button button = new Button("Cancel");
        button.addClickListener(event -> close());
        return button;
    }

    private void handleSendButtonClick() {
        if (emailField.isEmpty() || emailField.isInvalid()) {
            emailField.setInvalid(true);
        } else {
            Notification.show("Reset instructions sent. Check your inbox.")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            close();
        }
    }
}
