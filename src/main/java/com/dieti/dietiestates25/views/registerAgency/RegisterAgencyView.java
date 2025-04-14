package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.annotations.forward_logged_user.ForwardLoggedUser;
import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@ForwardLoggedUser
@Route(value = "register-agency", layout = MainLayout.class)
@PageTitle("Register Your Agency")
public class RegisterAgencyView extends VerticalLayout {

    AgencyRequestsHandler agencyHandler = new AgencyRequestsHandler();

    Form form = new Form();
    TextField agencyName;
    TextField vatNumber;
    TextField firstName;
    TextField lastName;
    EmailField email;

    public RegisterAgencyView() {
        configureLayout();
        configureComponents();
    }

    private void createForm() {
        agencyName = new TextField("Agency Name");
        vatNumber = new TextField("VAT Number");
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new EmailField("Email");

        var section = new H4("Fill in your personal details");
        var hr = new Hr();

        form.add(agencyName, vatNumber, hr, section, firstName, lastName, email);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        form.setColspan(agencyName, 2);
        form.setColspan(vatNumber, 2);
        form.setColspan(hr, 2);
        form.setColspan(section, 2);
        form.setColspan(email, 2);

        form.setRequiredTrue(agencyName, vatNumber, firstName, lastName, email);
    }

    private void configureComponents() {
        var logo = new DietiEstatesLogo(true);
        var registerAgencyDiv = new DivContainer("600px", "auto");
        createForm();

        var register = new Button("Register");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickShortcut(Key.ENTER);
        setAlignSelf(Alignment.CENTER, register);
        register.setWidth("35%");

        register.addClickListener(event -> {
            if (form.areRequiredFieldsValid())
                agencyHandler.createAgency(
                        agencyName.getValue(),
                        vatNumber.getValue(),
                        firstName.getValue(),
                        lastName.getValue(),
                        email.getValue()
                );
        });

        registerAgencyDiv.add(
                new H3("Register your Agency"),
                form,
                register
        );

        add(logo, registerAgencyDiv);
    }

    private void configureLayout() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.CENTER);
        setSpacing(false);
    }
}
