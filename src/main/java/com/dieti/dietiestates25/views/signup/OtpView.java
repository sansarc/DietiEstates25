package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.services.AuthenticationService;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.ui_components.CustomDivCard;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.views.upload.utils.Response;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;


@Route("confirm-account")
public class OtpView extends VerticalLayout implements BeforeEnterObserver {

    DietiEstatesLogo logo = new DietiEstatesLogo("600px", "auto");
    CustomDivCard otpViewDiv = new CustomDivCard("400px", "auto");
    H3 title = new H3("Confirm your account");
    Span description = createDescription();
    TextField otpTextField = new TextField();
    Button confirmButton = createButton();

    private String email;
    private final AuthenticationService authenticationService;

    public OtpView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        configureLayout();
        configureComponents();

        add(logo, otpViewDiv);
    }

    private void configureComponents() {
        logo.getStyle().setMarginBottom("120px");
        otpTextField.setPlaceholder("Enter your OTP.");
        otpViewDiv.add(
                title,
                description,
                otpTextField,
                confirmButton
        );
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title);
        setAlignSelf(Alignment.END, confirmButton);
    }

    private Span createDescription() {
        var emailText = new Span(email);
        emailText.getStyle().setFontWeight(700);
        return new Span(new Text("We've sent an email to "), emailText, new Text(" with an OTP code. Check your inbox."));
    }

    private Button createButton() {
        var button = new Button("Confirm");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        button.addClickListener(event -> {
            if (otpTextField.isEmpty()) {
                otpTextField.setErrorMessage("This field cannot be empty.");
                otpTextField.setInvalid(true);
            }
            else confirmUser();
        });

        return button;
    }

    private void confirmUser() {
        Response confirmed = authenticationService.confirmUser(email, otpTextField.getValue());
        VaadinSession.getCurrent().setAttribute("email", null);
        Notification notification = new Notification(confirmed.getStatusMessage(), 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(confirmed.ok() ? NotificationVariant.LUMO_SUCCESS : NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        email = (String) session.getAttribute("email");
        System.out.println("Email: " + email);

        if (event.getTrigger() == NavigationTrigger.PAGE_LOAD)
            event.rerouteToError(NotFoundException.class);
    }
}
