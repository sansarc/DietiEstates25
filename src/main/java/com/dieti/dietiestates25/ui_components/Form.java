package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.select.Select;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Form extends FormLayout {

    public Form() {}
    public Form(Component...components) {
        super(components);
        getStyle().setMarginBottom("var(--lumo-space-l)");
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

    public void setRequiredTrue(Component... fields) {
        for (var field : fields) {
            if (field instanceof HasValue<?, ?> hasValue)
                hasValue.setRequiredIndicatorVisible(true);

        }
    }

    public boolean areRequiredFieldsValid() {
        List<TextFieldBase<?, ?>> requiredTextFields = new ArrayList<>();
        List<ComboBox<?>> requiredComboBoxes = new ArrayList<>();
        List<AbstractNumberField<?, ?>> requiredNumberFields = new ArrayList<>();
        List<PasswordField> passwordFields = new ArrayList<>();
        AtomicReference<Optional<EmailField>> emailField = new AtomicReference<>(Optional.empty());

        getChildren()
                .forEach(component -> {
                    if (component instanceof TextFieldBase<?, ?> tfb && tfb.isRequiredIndicatorVisible())
                        requiredTextFields.add(tfb);
                    if (component instanceof AbstractNumberField<?, ?> nf && nf.isRequiredIndicatorVisible())
                        requiredNumberFields.add(nf);
                    if (component instanceof ComboBox<?> cb && cb.isRequiredIndicatorVisible())
                        requiredComboBoxes.add(cb);
                    if (component instanceof PasswordField pf)
                        passwordFields.add(pf);
                    if (component instanceof EmailField ef && emailField.get().isEmpty())
                        emailField.set(Optional.of(ef));
        });

        // validate required text fields and combo boxes
        if (requiredTextFields.stream().anyMatch(AbstractField::isEmpty)
                || requiredComboBoxes.stream().anyMatch(ComboBox::isEmpty)) {
            NotificationFactory.error("Please fill in all required fields to continue.");
            return false;
        }

        // validate required number fields
        if (requiredNumberFields.stream()
                .anyMatch(nf -> nf.getValue() == null || nf.getValue().intValue() <= 0)) {
            NotificationFactory.error("All required numeric fields must have values greater than 0.");
            return false;
        }

        // validate passwords (only if exactly two password fields)
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

        // validate email field
        if (emailField.get().isPresent() && emailField.get().get().isInvalid()) {
            var email = emailField.get().get();
            email.setInvalid(true);
            email.setErrorMessage("Invalid email address.");
            return false;
        }

        return true;
    }

    public void clear() {
        getChildren()
                .map(HasValue.class::cast)
                .forEach(HasValue::clear);
    }

    public static class LocationForm extends Form {
        public final ComboBox<String> region;
        public final ComboBox<String> province;
        public final CitiesComboBox city;
        public final TextField address;

        public LocationForm() {
            region = new ComboBox<>("Region");
            province = new ComboBox<>("Province");
            city = new CitiesComboBox();
            address = new TextField("Address");
            address.setAllowedCharPattern("^[a-zA-Z0-9 ]+$");

            var adRequestsHandler = new AdRequestsHandler();

            region.setItems(adRequestsHandler.getRegions());
            region.addValueChangeListener(event ->
                    province.setItems(adRequestsHandler.getProvinces(region.getValue()))
            );

            var cities = new ArrayList<City>();
            province.addValueChangeListener(event -> {
                cities.addAll(adRequestsHandler.getCities(province.getValue()));
                city.setItems(cities.stream().toList());
            });
        }

        public String getRegion() {
            return region.getValue() == null ? "" : region.getValue();
        }
        public String getProvince() {
            return province.getValue() == null ? "" : province.getValue();
        }

        public String getCity() {
            return city.getValue() == null ? "" : city.getValue().getCode();
        }

        public String getAddress() {
            return address.getValue() == null ? "" : address.getValue();
        }

        public List<Component> asList() {
            return List.of(region, province, city, address);
        }

        public Component[] asArray() {
            return asList().toArray(new Component[0]);
        }
    }


}
