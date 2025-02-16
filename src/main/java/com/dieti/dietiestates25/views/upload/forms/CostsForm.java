package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.views.upload.specific_components.OtherCostsEntryForm;
import com.dieti.dietiestates25.views.upload.utils.FormFieldFactory;
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

    @Override
    protected void configureLayout() {
        setWidth("40%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
    }

    @Override
    protected void createComponents() {
        price = FormFieldFactory.createPriceInEuroNumberField("Price", true);
        hidePrice = new Checkbox("Hide Price");
        condominiumFees = FormFieldFactory.createPriceInEuroNumberField("Condominium Fees", false);
        otherCostsEntryForm = new OtherCostsEntryForm();
    }

    @Override
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
