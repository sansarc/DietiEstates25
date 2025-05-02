package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.ad.City;
import com.vaadin.flow.component.combobox.ComboBox;

public class CitiesComboBox extends ComboBox<City> {

    public CitiesComboBox() { super("City"); }

    // find city name from code
    public void setValue(String code) {
        if (code == null) {
            setValue((City) null);
            return;
        }

        var items = getListDataView()
                .getItems()
                .toList();

        var matchingCity = items.stream()
                .filter(city -> code.equals(city.getCode()))
                .findFirst();

        if (matchingCity.isPresent())
            setValue(matchingCity.get());
        else
            setValue((City) null);
    }

}
