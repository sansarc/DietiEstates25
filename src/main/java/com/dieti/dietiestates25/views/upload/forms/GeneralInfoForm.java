package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.ui_components.InfoPopover;
import com.dieti.dietiestates25.views.upload.utils.FloorUtils;
import com.dieti.dietiestates25.views.upload.utils.FormFieldFactory;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class GeneralInfoForm extends UploadForm {
    private ComboBox<String> propertyType;
    private RadioButtonGroup<String> saleType;
    private Checkbox availability;
    private ComboBox<String> city;
    private TextField address;
    private NumberField streetNumber;
    private NumberField surfaceArea;
    private ComboBox<String> floor;
    private IntegerField numberOfFloors;

    public GeneralInfoForm() {
        configureLayout();
        createComponents();
        addComponents();
        setRequiredIndicatorVisibleTrue(propertyType, saleType, city, address, streetNumber, surfaceArea, floor);
    }

    @Override
    protected void createComponents() {
        propertyType = createPropertyTypeComboBox();
        saleType = createSaleTypeRadioGroup();
        availability = new Checkbox("Immediate availability");
        new InfoPopover(availability, "Check this option if the property is available for immediate occupancy or possession by the client without additional delays.");
        city = new ComboBox<>("City");
        address = new TextField("Address");
        streetNumber = createStreetNumberField();
        surfaceArea = createSurfaceAreaField();
        floor = createFloorsComboBox();
        numberOfFloors = createNumberOfFloorsField();
    }

    @Override
    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3)
        );
    }

    @Override
    protected void addComponents() {
        add(
                propertyType,
                saleType,
                availability,
                city,
                address,
                streetNumber,
                surfaceArea,
                floor,
                numberOfFloors
        );

    }

    private ComboBox<String> createPropertyTypeComboBox() {
        var comboBox = new ComboBox<String>("Property Type");
        comboBox.setItems("Villa", "Apartment", "Attic", "Single room");
        return comboBox;
    }

    private RadioButtonGroup<String> createSaleTypeRadioGroup() {
        var radioGroup = new RadioButtonGroup<String>("Type");
        radioGroup.setItems("For Sale", "Rent");
        radioGroup.setValue("For Sale");
        return radioGroup;
    }

    private NumberField createStreetNumberField() {
        var field = new NumberField("Street Number");
        field.setErrorMessage("This is clearly not a number.");
        return field;
    }

    private NumberField createSurfaceAreaField() {
        var field = new NumberField("Surface area");
        field.setSuffixComponent(new Div("m²"));
        field.setPlaceholder("example: 110");
        return field;
    }

    private ComboBox<String> createFloorsComboBox() {
        var comboBox = new ComboBox<String>("Floor number");
        comboBox.setItems(FloorUtils.generateFloorsList());
        return comboBox;
    }

    private IntegerField createNumberOfFloorsField() {
        var field = FormFieldFactory.createIntegerField("Total number of floors");
        field.setHelperText("In case of a single building, type the same number twice.");
        return field;
    }
}
