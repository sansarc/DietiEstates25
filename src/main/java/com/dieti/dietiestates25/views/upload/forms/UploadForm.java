package com.dieti.dietiestates25.views.upload.forms;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldBase;

public abstract class UploadForm extends FormLayout {

    protected UploadForm() {
        configureBase();
    }

    public IntegerField integerField(String label) {
        var field = new IntegerField(label);
        field.setMin(0);
        field.setValue(0);
        field.setStepButtonsVisible(true);
        return field;
    }

    public NumberField priceInEuroNumberField(String label, boolean isRequired) {
        var numberField = new NumberField(label);
        numberField.setPrefixComponent(new Icon(VaadinIcon.EURO));
        numberField.setErrorMessage("That's clearly not a positive number.");
        numberField.setRequiredIndicatorVisible(isRequired);
        numberField.setMin(0);
        return numberField;
    }

    private void configureBase() {
        getStyle().setMarginBottom("var(--lumo-space-l)");
    }

    protected final void setRequiredTrue(HasValue<?,?>...fields) {
        for (HasValue<?,?> field : fields) {
            field.setRequiredIndicatorVisible(true);
        }
    }

    public boolean areRequiredFieldsValid() {
        return getChildren()
                .filter(TextFieldBase.class::isInstance)
                .map(TextFieldBase.class::cast)
                .noneMatch(AbstractField::isEmpty);
    }

}
