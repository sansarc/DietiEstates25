package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.AbstractNumberField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.component.select.Select;

public class Form extends FormLayout {

    protected Form() {
        configureBase();
    }

    protected RadioButtonGroup<String> radioButtonGroup(String label, String...fields) {
        var radioGroup = new RadioButtonGroup<String>(label);
        radioGroup.setItems(fields);
        radioGroup.getChildren()
                .forEach(field ->
                        field.getStyle().setFontSize("14px")
                );

        return radioGroup;
    }

    protected Select<String> select(String label, String...fields) {
        var select = new Select<String>();
        select.setLabel(label);
        select.setItems(fields);
        return select;
    }

    protected CheckboxGroup<String> checkboxGroup(String label, String...fields) {
        var checkboxGroup = new CheckboxGroup<String>(label);
        checkboxGroup.setItems(fields);
        checkboxGroup.getChildren()
                .forEach(field ->
                        field.getStyle().setFontSize("14px")
                );
        return checkboxGroup;
    }

    protected ComboBox<String> comboBox(String label, String...fields) {
        var comboBox = new ComboBox<String>(label);
        comboBox.setItems(fields);
        return comboBox;
    }

    public static IntegerField integerField(String label, Component icon) {
        var field = integerField(label);
        if (icon instanceof Image) {
            ((Image) icon).setHeight("18px");
            ((Image) icon).setWidth("18px");
        }
        field.setSuffixComponent(icon);
        return field;
    }

    public static IntegerField integerField(String label) {
        var field = new IntegerField(label);
        field.setMin(0);
        field.setValue(0);
        field.setStepButtonsVisible(true);
        return field;
    }

    public static NumberField priceInEuroNumberField(String label, boolean isRequired) {
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
        boolean textFieldsValid = getChildren()
                .filter(TextFieldBase.class::isInstance)
                .map(TextFieldBase.class::cast)
                .noneMatch(AbstractField::isEmpty);

        boolean numberFieldsValid = getChildren()
                .filter(AbstractNumberField.class::isInstance)
                .map(AbstractNumberField.class::cast)
                .allMatch(field -> field.getValue().intValue() > 0);

        return textFieldsValid && numberFieldsValid;
    }

}
