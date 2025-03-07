package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.NumberField;

public class CostsForm extends Form {

    private NumberField price;
    private Checkbox hidePrice;
    private NumberField condominiumFees;

    public CostsForm() {
        configureLayout();
        createComponents();
        addComponents();
    }

    protected void configureLayout() {
        setWidth("40%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
    }

    protected void createComponents() {
        price = priceInEuroNumberField("Price", true);
        hidePrice = new Checkbox("Hide Price");
        condominiumFees = priceInEuroNumberField("Condominium Fees", false);
    }

    protected void addComponents() {
        add(
                price,
                hidePrice,
                condominiumFees
        );

        setColspan(hidePrice, 2);
    }
}
