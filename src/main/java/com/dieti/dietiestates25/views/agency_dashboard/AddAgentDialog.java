package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class AddAgentDialog extends Dialog {

    transient AgencyRequestsHandler agencyRequestsHandler = new AgencyRequestsHandler();

    public AddAgentDialog() {
        setHeaderTitle("New Agent");

        var firstName = new TextField("First Name");
        var lastName = new TextField("Last Name");
        var email = new EmailField("Email");

        var form = new Form(firstName, lastName, email);
        form.setRequiredTrue(firstName, lastName, email);
        form.setColspan(email, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        var add = new Button("Add", event -> {
            if (form.areRequiredFieldsValid())
                agencyRequestsHandler.createAgent(firstName.getValue(), lastName.getValue(), email.getValue());
        });
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickShortcut(Key.ENTER);

        var cancel = new Button("Cancel", event -> close());

        getFooter().add(cancel, add);
        add(form);

        configureLayout();
    }

    private void configureLayout() {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        addDialogCloseActionListener(event -> close());
    }

}
