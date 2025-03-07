package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.FloorUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GeneralInfoForm extends Form {

    private final RestTemplate restTemplate = new RestTemplate();

    public GeneralInfoForm() {
        configureLayout();
        createComponents();
    }

    protected void createComponents() {
        var saleType =  radioButtonGroup("Sale Type", "For Sale", " For Rent");

        var nation = new ComboBox<>("Nation");
        var region = new ComboBox<>("Region");
        var city = new ComboBox<>("City");

        nation.setItems(
                Arrays.stream(Locale.getISOCountries())
                        .map(code -> new Locale("", code).getDisplayCountry())
                        .sorted()
                        .collect(Collectors.toList())
        );

        nation.addValueChangeListener(event -> {
            String country = event.getValue().toString().toLowerCase();
            String countryCode = "";
            for (String code : Locale.getISOCountries()) {
                if (new Locale(code).getDisplayCountry().toLowerCase().equals(country))
                    countryCode = code;
                System.out.println(countryCode);
            }

            List<String> regions = getRegions(countryCode);
            region.setItems(regions);
            region.clear();
            city.clear();
        });

        city.addValueChangeListener(event -> {
        });

        var address = new TextField("Address");
        var zipcode = new NumberField("Zip Code");
        zipcode.setAllowedCharPattern("[0-9]");
        zipcode.setMin(5);
        zipcode.setMax(10);

        var dimension = new NumberField("Surface area");
        dimension.setSuffixComponent(new Div("m²"));
        dimension.setPlaceholder("example: 110");

        var floor = new Select<String>();
        floor.setLabel("Floor");
        floor.setItems(FloorUtils.generateFloorsList());

        add(
                saleType,
                nation,
                city,
                region,
                address,
                zipcode,
                dimension,
                floor
        );

        setRequiredTrue(saleType, nation, region, city, address, zipcode, dimension, floor);
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 3)
        );
    }

    private List<String> getCities(String countryCode, String regionCode) {
        String url = String.format("%s/countries/%s/regions/%s/cities", Constants.ApiEndpoints.BASE_URL, countryCode, regionCode);
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        return response.getBody();
    }

    private List<String> getRegions(String countryCode) {
        String url = String.format("%s/countries/%s/regions", Constants.ApiEndpoints.BASE_URL, countryCode);
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        return response.getBody();
    }

}
