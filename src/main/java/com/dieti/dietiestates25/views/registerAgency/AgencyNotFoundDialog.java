package com.dieti.dietiestates25.views.registerAgency;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;

public class AgencyNotFoundDialog extends Dialog {
    Button closeButton = new Button("Close", event -> close());

    public AgencyNotFoundDialog(String vatNumber) {
        configureLayout();
        configureComponents();

        Span helperText = createHelperText(vatNumber);
        add(helperText);
    }

    private void configureLayout() {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        addDialogCloseActionListener(event -> close());
    }

    private void configureComponents() {
        setHeaderTitle("VAT number not found");
        getFooter().add(closeButton);
        closeButton.setWidth("50%");
    }

    private Span createHelperText(String vatNumber) {
        var vatNumberText  = new Span(vatNumber);
        vatNumberText.getStyle()
                .setFontWeight(Style.FontWeight.BOLD)
                .setColor("red");

        return new Span(new Text("The VAT number "), vatNumberText, new Text(" could not be found."));
    }
}

