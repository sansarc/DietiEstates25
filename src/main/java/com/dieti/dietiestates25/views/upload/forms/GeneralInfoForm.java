package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.FloorUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;

public class GeneralInfoForm extends Form {

    RadioButtonGroup<String> saleType;
    LocationForm locationComponents;
    NumberField dimension;
    Select<String> floor;

    public GeneralInfoForm() {
        configureLayout();
        configureComponents();
    }

    protected void configureComponents() {
        saleType =  radioButtonGroup("Sale Type", "For Sale", " For Rent");
        saleType.setValue("For Sale");
        locationComponents = new LocationForm();
        dimension = new NumberField("Surface area");
        dimension.setSuffixComponent(new Div("m²"));
        dimension.setPlaceholder("example: 110");
        floor = select("Floor", FloorUtils.generateFloorsList().toArray(new String[0]));

        add(saleType);
        add(locationComponents.asList());
        add(dimension, floor);

        setRequiredTrue(locationComponents.asArray());
        setRequiredTrue(saleType, dimension);
    }

    public void addFormValues(AdInsert ad) {
        ad.setType((saleType.getValue().substring(4, 5))); // S for Sale or R for Rent
        ad.setCity(locationComponents.city.getValue().getCode());
        ad.setAddress(locationComponents.getAddress());
        ad.setDimensions(dimension.getValue().intValue());
        ad.setFloor(FloorUtils.parseFloorToInt(floor.getValue()));
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3)
        );
    }

}
