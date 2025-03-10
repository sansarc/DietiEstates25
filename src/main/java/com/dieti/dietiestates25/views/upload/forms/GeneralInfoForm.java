package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdRequest;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.FloorUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.web.client.RestTemplate;

public class GeneralInfoForm extends Form {

    private final RestTemplate restTemplate = new RestTemplate();

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

        region = new ComboBox<>("Region");
        city = new ComboBox<>("City");
        address = new TextField("Address");
        zipcode = new NumberField("Zip Code");
        zipcode.setAllowedCharPattern("[0-9]");
        zipcode.setMin(5);
        zipcode.setMax(10);

        dimension = new NumberField("Surface area");
        dimension.setSuffixComponent(new Div("m²"));
        dimension.setPlaceholder("example: 110");

        floor = new Select<>();
        floor.setLabel("Floor");
        floor.setItems(FloorUtils.generateFloorsList());

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
    }

    public void addFormValues(AdRequest ad) {
        ad.setSaleType(saleType.getValue());
        ad.getLocation().setRegion(region.getValue());
        ad.getLocation().setCity(city.getValue());
        ad.getLocation().setAddress(address.getValue());
        ad.getLocation().setZipcode(zipcode.getValue().toString());
        ad.setDimension(dimension.getValue().intValue());
        ad.setFloor(FloorUtils.parseFloor(floor.getValue()));
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3)
        );
    }

}
