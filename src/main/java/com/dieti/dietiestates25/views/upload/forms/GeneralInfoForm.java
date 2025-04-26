package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.FloorUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class GeneralInfoForm extends Form {

    RadioButtonGroup<String> saleType;
    ComboBox<String> region, province;
    ComboBox<City> city;
    TextField address;
    NumberField dimension;
    Select<String> floor;

    AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    public GeneralInfoForm() {
        configureLayout();
        configureComponents();
    }

    protected void configureComponents() {
        saleType =  radioButtonGroup("Sale Type", "For Sale", " For Rent");
        saleType.setValue("For Sale");
        region = new ComboBox<>("Region");
        province = new ComboBox<>("Province");
        city = new ComboBox<>("City");

        address = new TextField("Address");
        address.setAllowedCharPattern("^[a-zA-Z0-9 ]+$");

        dimension = new NumberField("Surface area");
        dimension.setSuffixComponent(new Div("m²"));
        dimension.setPlaceholder("example: 110");

        floor = select("Floor", FloorUtils.generateFloorsList().toArray(new String[0]));

        add(
                saleType,
                region,
                province,
                city,
                address,
                dimension,
                floor
        );

        setRequiredTrue(saleType, region, province, city, address, dimension);

        region.setItems(adRequestsHandler.getRegions());
        region.addValueChangeListener(event -> province.setItems(adRequestsHandler.getProvinces(region.getValue())));

        var cities = new ArrayList<City>();
        province.addValueChangeListener(event -> {
            cities.addAll(adRequestsHandler.getCities(province.getValue()));
            city.setItems(
                    cities.stream()
                    .toList()
            );
        });
    }

    public void addFormValues(AdInsert ad) {
        ad.setType((saleType.getValue().substring(4, 5))); // S for Sale or R for Rent
        ad.setCity(city.getValue().getCode());
        ad.setAddress(address.getValue());
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
