package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.dto.UserSession;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.PasswordField;

public class ConfirmAccountDialog extends Dialog {

    VerticalLayout layout;
    final AuthenticationHandler authHandler = new AuthenticationHandler();

    public ConfirmAccountDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        addDialogCloseActionListener(event -> close());

        layout = new VerticalLayout();
        add(layout);

        // Lazy-loading components to avoid weird logging blob
        addOpenedChangeListener(event -> {
            if (event.isOpened())
                UI.getCurrent().access(this::initializeComponents);

        });
    }

    private void initializeComponents() {
        layout.removeAll();  // preventing duplication
        setHeaderTitle("Create a new password");

        var password = new PasswordField("New password");
        var confirmPassword = new PasswordField("Confirm new password");

        password.setAutocomplete(Autocomplete.NEW_PASSWORD);
        confirmPassword.setAutocomplete(Autocomplete.NEW_PASSWORD);

        var form = new Form(password, confirmPassword);
        form.setRequiredTrue(password, confirmPassword);

        var confirmButton = new Button("Confirm");
        confirmButton.addClickListener(event -> {
            if (form.areRequiredFieldsValid())
                authHandler.confirmUser(UserSession.getEmail(), UserSession.getPwd(), password.getValue());
        });
        confirmButton.addClickShortcut(Key.ENTER);

        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        layout.add(form);
        getFooter().removeAll();
        getFooter().add(confirmButton);
    }
}
