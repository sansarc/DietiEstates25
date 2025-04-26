package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.select.Select;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Form extends FormLayout {

    public Form() {
        getStyle().setMarginBottom("var(--lumo-space-l)");
    }

    public Form(Component...components) {
        super(components);
        getStyle().setMarginBottom("var(--lumo-space-l)");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        getChildren()
                .filter(TextFieldBase.class::isInstance)
                .forEach(field ->
                        ((TextFieldBase<?, ?>) field).addValueChangeListener(event -> {
                            if (((TextFieldBase<?, ?>) field).getValue().toString().isEmpty())
                                getElement().executeJs("window.unsavedChanges = false;");
                            else
                                getElement().executeJs("window.unsavedChanges = true;");
                        })
                );

        attachEvent.getUI().getPage().executeJs(
                "console.log('beforeunload listener attached');" +
                        "window.unsavedChanges = false;" +
                        "window.addEventListener('beforeunload', function(e) {" +
                        "  console.log('beforeunload event fired, unsavedChanges:', window.unsavedChanges);" +
                        "  if(window.unsavedChanges) {" +
                        "    e.preventDefault();" +
                        "    e.returnValue = '';" +
                        "  }" +
                        "});"
        );
    }


    public static RadioButtonGroup<String> radioButtonGroup(String label, String...fields) {
        var radioGroup = new RadioButtonGroup<String>(label);
        radioGroup.setItems(fields);
        radioGroup.getChildren()
                .forEach(field ->
                        field.getStyle().setFontSize("14px")
                );

        return radioGroup;
    }

    public static Select<String> select(String label, String...fields) {
        var select = new Select<String>();
        select.setLabel(label);
        select.setItems(fields);
        return select;
    }

    public static CheckboxGroup<String> checkboxGroup(String label, String...fields) {
        var checkboxGroup = new CheckboxGroup<String>(label);
        checkboxGroup.setItems(fields);
        checkboxGroup.getChildren()
                .forEach(field ->
                        field.getStyle().setFontSize("14px")
                );
        return checkboxGroup;
    }

    public static ComboBox<String> comboBox(String label, String...fields) {
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

    public static NumberField priceInEuroNumberField(String label) {
        var numberField = new NumberField(label);
        numberField.setPrefixComponent(new Icon(VaadinIcon.EURO));
        numberField.setPlaceholder("0.00");
        numberField.setMin(0);

        numberField.setAllowedCharPattern("[0-9\\.]");
        return numberField;
    }

    private Stream<Component> getAllComponents(Component parent) {
        return Stream.concat(
                parent.getChildren(),
                parent.getChildren().flatMap(this::getAllComponents)
        );
    }


    public void setRequiredTrue(HasValue<?,?>...fields) {
        for (HasValue<?,?> field : fields) {
            field.setRequiredIndicatorVisible(true);
        }
    }

    public boolean areRequiredFieldsValid() {

        boolean textFieldsValid = getAllComponents(this)
                .filter(TextFieldBase.class::isInstance)
                .map(TextFieldBase.class::cast)
                .filter(TextFieldBase::isRequiredIndicatorVisible)
                .noneMatch(AbstractField::isEmpty);

        if (!textFieldsValid) {
            Notification.show("Please fill in all required text fields to continue.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        boolean numberFieldsValid = getAllComponents(this)
                .filter(AbstractNumberField.class::isInstance)
                .map(AbstractNumberField.class::cast)
                .filter(TextFieldBase::isRequiredIndicatorVisible)
                .allMatch(field -> field.getValue() != null && field.getValue().intValue() > 0);

        if (!numberFieldsValid) {
            Notification.show("All required numeric fields must have values greater than 0")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        // password fields validation
        List<PasswordField> passwordFields = getAllComponents(this)
                .filter(PasswordField.class::isInstance)
                .map(PasswordField.class::cast)
                .toList();

        if (passwordFields.size() == 2) {
            var password = passwordFields.get(0);
            var confirmPassword = passwordFields.get(1);

            if (!Objects.equals(password.getValue(), confirmPassword.getValue())) {
                password.setInvalid(true);
                confirmPassword.setInvalid(true);
                confirmPassword.setErrorMessage("Passwords do not match.");
                return false;
            }
        }

        // email validation
        var email = getAllComponents(this)
                .filter(EmailField.class::isInstance)
                .map(EmailField.class::cast)
                .findFirst();

        if (email.isPresent() && email.get().isInvalid()) {
            email.get().setInvalid(true);
            email.get().setErrorMessage("Invalid email address.");
            return false;
        }

        return true;
    }




}
