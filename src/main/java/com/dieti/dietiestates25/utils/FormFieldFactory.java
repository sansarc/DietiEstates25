package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

public class FormFieldFactory {

    private FormFieldFactory() {}

    public static IntegerField createIntegerField(String label) {
        var field = new IntegerField(label);
        field.setMin(0);
        field.setValue(0);
        field.setStepButtonsVisible(true);
        return field;
    }

    public static NumberField createPriceInEuroNumberField(String label, boolean isRequired) {
        var numberField = new NumberField(label);
        numberField.setPrefixComponent(new Icon(VaadinIcon.EURO));
        numberField.setErrorMessage("That's clearly not a positive number.");
        numberField.setRequiredIndicatorVisible(isRequired);
        numberField.setMin(0);
        return numberField;
    }
}