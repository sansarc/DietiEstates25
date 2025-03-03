package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.annotations.ForwardLoggedUser;
import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.utils.NotificationFactory;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.util.concurrent.atomic.AtomicBoolean;

@ForwardLoggedUser
@Route("register-agency")
public class RegisterAgencyView extends VerticalLayout {

    FormLayout formLayout;
    private static BeanValidationBinder<RegisterAgencyRequest> binder = new BeanValidationBinder<>(RegisterAgencyRequest.class);

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

        binder.bindInstanceFields(this);

        register.addClickListener(event -> {
            if (binder.validate().hasErrors()) {
                NotificationFactory.error("Please fill all required fields");
            } else {
                // Proceed with submission
            }
        });

        registerAgencyDiv.add(
                new H3("Register your Agency"),
                formLayout,
                register
        );

        add(logo, registerAgencyDiv);
    }

    private static FormLayout getFormLayout() {
        var agencyName = new TextField("Agency Name");
        var vatNumber = new TextField("VAT Number");
        var firstName = new TextField("First Name");
        var lastName = new TextField("Last Name");
        var email = new EmailField("Email");
        var password = new PasswordField("Password");
        var confirmPassword = new PasswordField("Confirm Password");
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

        return formLayout;
    }

    private void configureLayout() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.CENTER);
        setSpacing(false);
    }

    public void setAgency(RegisterAgencyRequest agency) {
        binder.setBean(agency);
    }
}
