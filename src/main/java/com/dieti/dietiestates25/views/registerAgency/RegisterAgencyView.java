package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.annotations.ForwardLoggedUser;
import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.router.Route;

@ForwardLoggedUser
@Route("register-agency")
public class RegisterAgencyView extends VerticalLayout {

    AgencyRequestsHandler agencyHandler = new AgencyRequestsHandler();

    FormLayout formLayout;
    TextField agencyName;
    TextField vatNumber;
    TextField firstName;
    TextField lastName;
    EmailField email;
    PasswordField password;
    PasswordField confirmPassword;

    public RegisterAgencyView() {
        configureLayout();
        configureComponents();
        attachUnsavedChangesListeners();

        addAttachListener(event -> event.getUI().getPage().executeJs(
                "console.log('beforeunload listener attached');" +
                        "window.unsavedChanges = false;" +
                        "window.addEventListener('beforeunload', function(e) {" +
                        "  console.log('beforeunload event fired, unsavedChanges:', window.unsavedChanges);" +
                        "  if(window.unsavedChanges) {" +
                        "    e.preventDefault();" +
                        "    e.returnValue = '';" +
                        "  }" +
                        "});"
        ));
    }

    private void attachUnsavedChangesListeners() {
        formLayout.getChildren()
                .filter(TextFieldBase.class::isInstance)
                .forEach(field ->
                    ((TextFieldBase<?, ?>) field).addValueChangeListener(event -> {
                        if (((TextFieldBase<?, ?>) field).getValue().equals(""))
                            getElement().executeJs("window.unsavedChanges = false; ");
                        else
                            getElement().executeJs("window.unsavedChanges = true; ");
                    })
                );
    }

    private void configureComponents() {
        var logo = new DietiEstatesLogo(true);
        var registerAgencyDiv = new DivContainer("600px", "auto");
        formLayout = getFormLayout();

        var register = new Button("Register");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickShortcut(Key.ENTER);
        setAlignSelf(Alignment.CENTER, register);
        register.setWidth("35%");



        register.addClickListener(event -> {
            boolean allFieldsValid = formLayout.getChildren()
                    .filter(TextFieldBase.class::isInstance)
                    .map(TextFieldBase.class::cast)
                    .noneMatch(AbstractField::isEmpty);

            if (allFieldsValid)
                agencyHandler.createAgency(
                        agencyName.getValue(),
                        vatNumber.getValue(),
                        firstName.getValue(),
                        lastName.getValue(),
                        email.getValue(),
                        password.getValue()
                );
            else
                 NotificationFactory.error("Please fill all required fields");
        });

        registerAgencyDiv.add(
                new H3("Register your Agency"),
                formLayout,
                register
        );

        add(logo, registerAgencyDiv);
    }

    private FormLayout getFormLayout() {

        agencyName = new TextField("Agency Name");
        vatNumber = new TextField("VAT Number");
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new EmailField("Email");
        password = new PasswordField("Password");
        confirmPassword = new PasswordField("Confirm Password");

        var secondTitle =  new H4("Fill in your personal information");
        secondTitle.getStyle().setPaddingTop("20px");

        var formLayout = new FormLayout(agencyName, vatNumber, secondTitle, firstName, lastName, email, password, confirmPassword);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setColspan(secondTitle, 2);
        formLayout.setColspan(email, 2);
        formLayout.setColspan(agencyName, 2);
        formLayout.setColspan(vatNumber, 2);

        formLayout.getChildren()
                .filter(TextFieldBase.class::isInstance)
                .forEach(field -> ((TextFieldBase<?, ?>) field).setRequired(true));

        HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<PasswordField, String>> listener = event -> {
            String passwordValue = password.getValue();
            String confirmPasswordValue = confirmPassword.getValue();
            if (!passwordValue.isEmpty() && !confirmPasswordValue.isEmpty()) {
                boolean valid = passwordValue.equals(confirmPasswordValue);
                confirmPassword.setInvalid(!valid);
                confirmPassword.setErrorMessage(valid ? null : "Passwords don't match");
            } else {
                confirmPassword.setInvalid(false);
                confirmPassword.setErrorMessage(null);
            }
        };

        password.addValueChangeListener(listener);
        confirmPassword.addValueChangeListener(listener);

        return formLayout;
    }

    private void configureLayout() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.CENTER);
        setSpacing(false);
    }
}
