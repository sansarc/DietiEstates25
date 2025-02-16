package com.dieti.dietiestates25.views.registerAgency;

import com.dieti.dietiestates25.views.ui_components.CustomDivCard;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register-agency")
public class RegisterAgencyView extends VerticalLayout {

    private final CustomDivCard registerAgencyDiv = new CustomDivCard("400px", "auto");
    private final TextField companyName = new TextField("Company Name");
    private final TextField VATNumberField = new TextField("VAT Number");
    private final H3 title = new H3("Register Your Agency");
    private final Button registerButton = createRegisterButton();
    private final Span descriptionText = createDescriptionText();

    public RegisterAgencyView() {

        configureLayout();
        configureComponents();

        add(new DietiEstatesLogo(), registerAgencyDiv);
    }

    private void configureComponents() {
        VATNumberField.setPlaceholder("ex.: 1234567 890 1");

        registerAgencyDiv.add(
                title,
                descriptionText,
                companyName,
                VATNumberField,
                registerButton
        );
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignSelf(Alignment.CENTER, title, registerButton, descriptionText);
    }

    private Button createRegisterButton() {
        Button button = new Button("Register");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setWidth("60%");
        button.addClickShortcut(Key.ENTER);

        button.addClickListener(event -> {
           if (companyName.isEmpty() || VATNumberField.getValue().isEmpty()) {
               companyName.setInvalid(true);
               VATNumberField.setInvalid(true);
           } else registerAgency();
        });

        return button;
    }

    private void registerAgency() {
        // else
        new AgencyNotFoundDialog(VATNumberField.getValue()).open();
    }

    private Span createDescriptionText() {
        String description = "Expand your client base.<br>More visibility, more clients.";
        String styledText = "<span style='font-style: italic'>"+description+"</span>";

        var span = new Span();
        span.getElement().setProperty("innerHTML", styledText);
        span.getStyle().setMarginTop("10px");
        span.getStyle().setMarginBottom("20px");
        span.getStyle().setColor("hsl(214, 100%, 43%)");  // lumo primary color
        return span;
    }

}
