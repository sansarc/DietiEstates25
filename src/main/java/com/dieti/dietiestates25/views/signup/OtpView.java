package com.dieti.dietiestates25.views.signup;

import com.dieti.dietiestates25.services.AuthenticationService;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.ui_components.CustomDivCard;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.dieti.dietiestates25.dto.Response;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;


@Route("confirm-account")
public class OtpView extends VerticalLayout implements BeforeEnterObserver {

    DietiEstatesLogo logo = new DietiEstatesLogo("600px", "auto");
    CustomDivCard otpViewDiv = new CustomDivCard("600px", "200px");
    H3 title = new H3("Confirm your account");
    Paragraph description = new Paragraph();
    TextField otpTextField = createOtpTextField();
    Button confirmButton = createButton();
    HorizontalLayout inputLayout = new HorizontalLayout(otpTextField, confirmButton);

    private String email;
    private final AuthenticationService authenticationService;

    public OtpView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        configureLayout();

        add(logo, otpViewDiv);
    }

    private void configureComponents() {
        logo.getStyle().setMarginBottom("80px");
        title.getStyle().setMarginBottom("20px");
        inputLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        otpViewDiv.add(
                title,
                description,
                inputLayout
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
        var emailText = new Span(email);
        emailText.getStyle().setFontWeight(Style.FontWeight.BOLDER);

        Paragraph paragraph = new Paragraph(new Text("We've sent a confirmation code to "), emailText, new Text(".\nCheck your inbox."));
        paragraph.getStyle().set("white-space", "pre-line");   // enables \n

        return paragraph;
    }

    private Button createButton() {
        var button = new Button("Confirm");
        button.setWidth("20%");
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
        Notification notification = new Notification(confirmed.getStatusMessage(), 5000, Notification.Position.TOP_CENTER   );
        if (confirmed.ok()) {
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(HomeView.class);
        }
        notification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null && event.getTrigger() != NavigationTrigger.PAGE_LOAD) {
            email = (String) session.getAttribute("email");
            System.out.println("Email in beforeEnter: " + email);
            description = createDescription();
            configureComponents();
        }
        else
            event.rerouteToError(NotFoundException.class);
    }
}
