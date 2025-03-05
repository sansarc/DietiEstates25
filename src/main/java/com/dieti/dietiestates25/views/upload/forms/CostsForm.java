package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.views.upload.specific_components.OtherCostsEntryForm;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.NumberField;

public class CostsForm extends UploadForm {

    private NumberField price;
    private Checkbox hidePrice;
    private NumberField condominiumFees;
    private OtherCostsEntryForm otherCostsEntryForm;

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
        otherCostsEntryForm = new OtherCostsEntryForm();
    }

    protected void addComponents() {
        add(
                price,
                hidePrice,
                condominiumFees,
                otherCostsEntryForm
        );

        setColspan(hidePrice, 2);
        setColspan(otherCostsEntryForm, 2);
    }
}
