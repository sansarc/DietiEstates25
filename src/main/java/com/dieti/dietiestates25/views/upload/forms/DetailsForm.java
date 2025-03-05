package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.ui_components.InfoPopover;
import com.dieti.dietiestates25.views.upload.specific_components.RadioButtonGroupCustomFontSize;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;


public class DetailsForm extends UploadForm {
    public static final int N_OF_COLUMNS = 4;

    private IntegerField elevators;
    private IntegerField bedrooms;
    private IntegerField livingRooms;
    private IntegerField kitchens;
    private IntegerField bathrooms;
    private Checkbox disabledBathroom;
    private CheckboxGroup<String> otherDetails;
    private IntegerField garageSpots;
    private IntegerField externalSpots;
    private Popover parkingInfo;
    private RadioButtonGroup<String> furnitureType;
    private ComboBox<String> heatingSystem;
    private ComboBox<String> propertyStatus;

    public DetailsForm() {
        configureLayout();
        createComponents();
        addComponents();
    }

    @Override
    protected void createComponents() {
        elevators = createIntegerFieldWithIcon("Elevators", new Image("/images/elevator.png", "elevator_icon"));
        bedrooms = createIntegerFieldWithIcon("Bedrooms", new Icon(VaadinIcon.BED));
        livingRooms = createIntegerFieldWithIcon("Living rooms", new Image("/images/sofa-512.png", "sofa_icon"));
        kitchens = createIntegerFieldWithIcon("Kitchens", new Icon(VaadinIcon.CUTLERY));
        bathrooms = createIntegerFieldWithIcon("Bathrooms", new Image("/images/shower-512.png", "shower_icon"));
        disabledBathroom = createDisabledBathroomCheckbox();
        otherDetails = createOtherDetailsCheckboxGroup();
        garageSpots = createIntegerFieldWithIcon("Parking spots in a garage", new Icon(VaadinIcon.CAR));
        externalSpots = createIntegerFieldWithIcon("External parking spots", new Icon(VaadinIcon.CAR));
        parkingInfo = new InfoPopover(externalSpots, "In this category are NOT included parking spots in the road or lots in stores or parks nearby, ONLY parking spots within the property.");
        furnitureType = createFurnitureTypeRadioGroup();
        heatingSystem = createHeatingSystemComboBox();
        propertyStatus = createPropertyStatusComboBox();
    }

    private ComboBox<String> createPropertyStatusComboBox() {
        var comboBox = new ComboBox<String>("Property status");
        comboBox.setItems("New / In construction", "Excellent / Renovated", "Good", "To renovate");
        return comboBox;
    }

    private ComboBox<String> createHeatingSystemComboBox() {
        var comboBox = new ComboBox<String>("Heating System / AC");
        comboBox.setItems("Independent", "Centralized");
        return comboBox;
    }

    private RadioButtonGroup<String> createFurnitureTypeRadioGroup() {
        var radioGroup = new RadioButtonGroupCustomFontSize<String>("Furniture Type");
        radioGroup.setItems("Furnished", "Unfurnished");
        radioGroup.resize("14px");
        return radioGroup;
    }

    @Override
    protected void configureLayout() {
        setWidth("90%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", N_OF_COLUMNS)
        );
    }

    @Override
    protected void addComponents() {
        add(
                elevators,
                bedrooms,
                livingRooms,
                kitchens,
                bathrooms,
                disabledBathroom,
                furnitureType,
                heatingSystem,
                otherDetails,
                garageSpots,
                externalSpots,
                parkingInfo,
                propertyStatus
        );

        setColspan(otherDetails, 4);
    }

    private IntegerField createIntegerFieldWithIcon(String label, Component icon) {
        var field = integerField(label);
        if (icon instanceof Image) {
            ((Image) icon).setHeight("18px");
            ((Image) icon).setWidth("18px");
        }
        field.setSuffixComponent(icon);
        return field;
    }

    private CheckboxGroup<String> createOtherDetailsCheckboxGroup() {
        var checkboxGroup = new CheckboxGroup<String>("Other details");
        checkboxGroup.setItems("Basement", "Terrace", "Balcony", "Garden", "Pool", "Alarm system");
        checkboxGroup.getChildren()
                .filter(child -> child instanceof Checkbox)
                .map(child -> (Checkbox)child)
                .forEach(checkbox -> checkbox.getStyle().set("font-size", "14px"));
        return checkboxGroup;
    }

    private Checkbox createDisabledBathroomCheckbox() {
        var checkbox = new Checkbox("One or more bathroom is for disabled.");
        checkbox.getStyle().set("font-size", "14px");
        return checkbox;
    }
}
