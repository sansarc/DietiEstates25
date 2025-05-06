package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.annotations.forward_guest.ForwardGuest;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.ui_components.ResendLink;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

@ForwardGuest
@Route("otp/:key")
public class OtpView extends VerticalLayout implements BeforeEnterObserver {

    private final String CONFIRM_ACCOUNT = "confirmAccount";
    private final String CHANGE_PWD = "changePassword";

    private String KEY;
    private String TITLE;

    DivContainer otpViewDiv = new DivContainer("600px", "250px");
    H3 title;
    Button confirmButton;
    TextField otpTextField;
    Form form;
    PasswordField password;
    PasswordField confirmPassword;
    ResendLink resendLink;
    Component inputLayout; // either Horizontal or Vertical layout

    transient AuthenticationHandler authHandler = new AuthenticationHandler();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll(); // prevents duplication

        var keyParam = event.getRouteParameters().get("key");

        if (keyParam.isPresent() &&
                (keyParam.get().equals(CHANGE_PWD) || keyParam.get().equals(CONFIRM_ACCOUNT)))
            KEY = keyParam.get();
        else
            event.forwardTo(PageNotFoundView.class);

        TITLE = KEY.equals(CHANGE_PWD) ? "Change Your Password" : "Confirm Your Account";

        UI.getCurrent().access(() ->   // change page title depending on the param
                UI.getCurrent().getPage().setTitle(TITLE)
        );

        configureComponents();
        configureLayout();
    }

    public OtpView() { /* for testing */ }

    private void configureComponents() {
        title = new H3(TITLE);
        title.getStyle().setMarginBottom("10px");

        var logo = new DietiEstatesLogo("600px", "auto");
        logo.getStyle().setMarginBottom("70px");

        var emailText = new Span(UserSession.getEmail());
        emailText.getStyle().setFontWeight(Style.FontWeight.BOLDER);

        var description = new Paragraph(new Text("We've sent an OTP code to "), emailText, new Text(".\nCheck your inbox."));
        description.getStyle()
                .setMarginBottom("-10px")
                .setWhiteSpace(Style.WhiteSpace.PRE_LINE);   // enables \n

        createOtpTextField();
        createResendLink();
        addPasswordFormIfChangePwd();
        createConfirmButton();

        inputLayout = KEY.equals(CHANGE_PWD)
                ? new VerticalLayout(form, confirmButton)
                : new HorizontalLayout(otpTextField, confirmButton);

        otpViewDiv.add(
                title,
                description,
                inputLayout,
                resendLink
        );

        add(logo, otpViewDiv);
    }

    private void addPasswordFormIfChangePwd() {
        if (KEY.equals(CHANGE_PWD)) {
            password = new PasswordField("New Password");
            confirmPassword = new PasswordField("Confirm Password");

            form = new Form(otpTextField, password, confirmPassword);
            form.setRequiredTrue(otpTextField, confirmPassword, otpTextField);
            form.setResponsiveSteps(
                    new Form.ResponsiveStep("0", 1),
                    new Form.ResponsiveStep("200px", 2)
            );
            form.setColspan(otpTextField, 2);

            resendLink.getStyle().setTransform("translateY(-220%)");
            otpViewDiv.setHeight("350px");
        }
    }

    private void createResendLink() {
        resendLink = new ResendLink(() -> {
            if (KEY.equals(CONFIRM_ACCOUNT))
                authHandler.recreateUser(UserSession.getFirstName(), UserSession.getLastName(), UserSession.getEmail(), UserSession.getPwd());
            else if (KEY.equals(CHANGE_PWD))
                authHandler.sendOTP(UserSession.getEmail());
            else
                NotificationFactory.criticalError("There was an unexpected error. Please try again later.");
        });
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

        if (inputLayout instanceof HorizontalLayout)
            ((HorizontalLayout) inputLayout).setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void createConfirmButton() {
        confirmButton = new Button("Confirm");
        confirmButton.setWidth("20%");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.getStyle().setCursor("pointer");

        confirmButton.addClickListener(event -> {
            if (otpTextField.getValue().isBlank()) {
                otpTextField.setErrorMessage("This field cannot be empty.");
                otpTextField.setInvalid(true);
            }
            else {
                if (KEY.equals(CONFIRM_ACCOUNT))
                    authHandler.confirmUser(UserSession.getEmail(), otpTextField.getValue());
                else if (KEY.equals(CHANGE_PWD) && form.areRequiredFieldsValid())
                    authHandler.changePwd(UserSession.getEmail(), password.getValue(), otpTextField.getValue());
                else
                    NotificationFactory.criticalError("There was an unexpected error. Please try again later.");
            }
        });
    }
}
