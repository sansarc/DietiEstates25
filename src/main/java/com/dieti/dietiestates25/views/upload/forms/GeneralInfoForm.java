package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.FloorUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class GeneralInfoForm extends Form {

    RadioButtonGroup<String> saleType;
    ComboBox<String> region, city;
    TextField address;
    NumberField zipcode, dimension;
    Select<String> floor;

    public GeneralInfoForm() {
        configureLayout();
        createComponents();
    }

    protected void createComponents() {
        saleType =  radioButtonGroup("Sale Type", "For Sale", " For Rent");
        saleType.setValue("For Sale");
        region = new ComboBox<>("Region");
        city = new ComboBox<>("City");
        address = new TextField("Address");
        zipcode = new NumberField("Zip Code");

        dimension = new NumberField("Surface area");
        dimension.setSuffixComponent(new Div("m²"));
        dimension.setPlaceholder("example: 110");

        floor = select("Floor", FloorUtils.generateFloorsList().toArray(new String[0]));

        add(
                saleType,
                region,
                city,
                address,
                zipcode,
                dimension,
                floor
        );

        setRequiredTrue(saleType, region, city, address, zipcode, dimension, floor);

        floor.setValue("ground floor");
        region.setItems("TEST");
        city.setItems("TEST");
    }

    public void addFormValues(Ad ad) {
        ad.setSaleType(saleType.getValue());
        ad.setRegion(region.getValue());
        ad.setCity(city.getValue());
        ad.setAddress(address.getValue());
        ad.setZipcode(zipcode.getValue().toString());
        ad.setDimension(dimension.getValue().intValue());
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
