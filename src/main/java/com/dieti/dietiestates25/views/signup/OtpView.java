package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.dto.User;
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
import com.vaadin.flow.server.VaadinSession;


@Route("confirm-account")
public class OtpView extends VerticalLayout implements BeforeEnterObserver {

    DietiEstatesLogo logo = new DietiEstatesLogo("600px", "auto");
    DivContainer otpViewDiv = new DivContainer("600px", "200px");
    H3 title = new H3("Confirm your account");
    Paragraph description = new Paragraph();
    TextField otpTextField = createOtpTextField();
    Button confirmButton = confirmButton();
    HorizontalLayout inputLayout = new HorizontalLayout(otpTextField, confirmButton);
    ResendLink resendLink;

    private final AuthenticationHandler authHandler = new AuthenticationHandler();
    private User user;

    public OtpView() {
        configureLayout();
        add(logo, otpViewDiv);
    }

    private void configureComponents() {
        logo.getStyle().setMarginBottom("80px");
        title.getStyle().setMarginBottom("20px");
        inputLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        resendLink = new ResendLink(() -> authHandler.recreateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPwd()));

        otpViewDiv.add(
                title,
                description,
                inputLayout,
                resendLink
        );
    }

    private TextField createOtpTextField() {
        var otpTextField = new TextField();
        otpTextField.setPlaceholder("Enter your OTP");
        otpTextField.setMaxLength(6);
        otpTextField.setMinLength(6);
        otpTextField.setWidth("40%");
        otpTextField.setAllowedCharPattern("[0-9]*");
        otpTextField.getStyle().setTextAlign(Style.TextAlign.CENTER);
        otpTextField.getStyle().setFontWeight(Style.FontWeight.BOLD);
        otpTextField.getStyle().setFontSize("18px");
        return otpTextField;
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title, otpTextField, confirmButton);
        setAlignSelf(Alignment.END, confirmButton);
    }

    private Paragraph createDescription() {
        var emailText = new Span(user.getEmail());
        emailText.getStyle().setFontWeight(Style.FontWeight.BOLDER);

        Paragraph paragraph = new Paragraph(new Text("We've sent a confirmation code to "), emailText, new Text(".\nCheck your inbox."));
        paragraph.getStyle().setWhiteSpace(Style.WhiteSpace.PRE_LINE);   // enables \n

        return paragraph;
    }

    private Button confirmButton() {
        var button = new Button("Confirm");
        button.setWidth("20%");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        button.addClickListener(event -> confirmUser());

        return button;
    }

    private void confirmUser() {
        if (otpTextField.getValue().isBlank()) {
            otpTextField.setErrorMessage("This field cannot be empty.");
            otpTextField.setInvalid(true);
        }
        else
            authHandler.confirmUser(user.getEmail(), otpTextField.getValue(), false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null && event.getTrigger() != NavigationTrigger.PAGE_LOAD) {
            user = (User) session.getAttribute("user");
            description = createDescription();
            configureComponents();
        }
        else
            event.rerouteToError(NotFoundException.class);
    }
}
