package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.annotations.forward_guest.ForwardGuest;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.ui_components.ResendLink;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

@ForwardGuest
@Route("confirm-account")
@PageTitle("Confirm Your Account")
public class OtpView extends VerticalLayout {

    DivContainer otpViewDiv = new DivContainer("600px", "250px");
    H3 title = new H3("Confirm your account");
    Button confirmButton;
    TextField otpTextField;
    HorizontalLayout inputLayout;

    final AuthenticationHandler authHandler = new AuthenticationHandler();

    public OtpView() {
        configureComponents();
        configureLayout();
    }

    private void configureComponents() {
        var logo = new DietiEstatesLogo("600px", "auto");

        logo.getStyle().setMarginBottom("70px");
        title.getStyle().setMarginBottom("10px");

        var emailText = new Span(UserSession.getEmail());
        emailText.getStyle().setFontWeight(Style.FontWeight.BOLDER);
        var description = new Paragraph(new Text("We've sent an OTP code to "), emailText, new Text(".\nCheck your inbox."));
        description.getStyle().setWhiteSpace(Style.WhiteSpace.PRE_LINE);   // enables \n

        createOtpTextField();
        createConfirmButton();
        inputLayout = new HorizontalLayout(otpTextField, confirmButton);

        var resendLink = new ResendLink(() -> authHandler.recreateUser(UserSession.getFirstName(), UserSession.getLastName(), UserSession.getEmail(), UserSession.getPwd()));

        otpViewDiv.add(
                title,
                description,
                inputLayout,
                resendLink
        );

        add(logo, otpViewDiv);
    }

    private void createOtpTextField() {
        otpTextField = new TextField("Enter your OTP");
        otpTextField.setMinLength(6);
        otpTextField.setMaxLength(6);
        otpTextField.setWidth("40%");
        otpTextField.setAllowedCharPattern("[0-9]*");
        otpTextField.getStyle().setTextAlign(Style.TextAlign.CENTER);
        otpTextField.getStyle().setFontWeight(Style.FontWeight.BOLD);
        otpTextField.getStyle().setFontSize("18px");
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        setAlignSelf(Alignment.CENTER, title, otpTextField, confirmButton);
        setAlignSelf(Alignment.END, confirmButton);

        inputLayout.setJustifyContentMode(JustifyContentMode.CENTER);

    }

    private void createConfirmButton() {
        confirmButton = new Button("Confirm");
        confirmButton.setWidth("20%");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);

        confirmButton.addClickListener(event -> {
            if (otpTextField.getValue().isBlank()) {
                otpTextField.setErrorMessage("This field cannot be empty.");
                otpTextField.setInvalid(true);
            }
            else
                authHandler.confirmUser(UserSession.getEmail(), otpTextField.getValue());
        });
    }
}
