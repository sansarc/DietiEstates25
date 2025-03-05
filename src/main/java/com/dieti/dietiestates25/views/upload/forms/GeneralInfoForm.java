package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.utils.FloorUtils;
import com.dieti.dietiestates25.views.upload.specific_components.RadioButtonGroupCustomFontSize;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class GeneralInfoForm extends UploadForm {

    public GeneralInfoForm() {
        configureLayout();
        createComponents();
    }

    protected void createComponents() {
        var saleType = createSaleTypeRadioGroup();
        var city = new ComboBox<>("City");
        var address = new TextField("Address");
        var streetNumber = createStreetNumberField();
        var surfaceArea = createSurfaceAreaField();
        var floor = createFloorsComboBox();
        var numberOfFloors = createNumberOfFloorsField();

        add(
                saleType,
                city,
                address,
                streetNumber,
                surfaceArea,
                floor,
                numberOfFloors
        );

        setRequiredTrue(saleType, city, address, streetNumber, surfaceArea, floor);
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3)
        );
    }

    private ComboBox<String> createPropertyTypeComboBox() {
        var comboBox = new ComboBox<String>("Property Type");
        comboBox.setItems("Villa", "Apartment", "Attic", "Single room");
        return comboBox;
    }

    private RadioButtonGroupCustomFontSize<String> createSaleTypeRadioGroup() {
        var radioGroup = new RadioButtonGroupCustomFontSize<String>("Sale Type");
        radioGroup.setItems("For Sale", "Rent");
        radioGroup.setValue("For Sale");
        radioGroup.resize("14px");
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
        var field = integerField("Total number of floors");
        field.setHelperText("In case of a single building, type the same number twice.");
        return field;
    }
}
