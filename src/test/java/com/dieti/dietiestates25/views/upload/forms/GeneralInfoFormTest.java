package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralInfoFormTest {

    private GeneralInfoForm form;

    @BeforeEach
    void setUp() {
        form = new GeneralInfoForm();
    }

    @Test
    void addFormValues_setsCorrectAdValues() {
        form.dimension.setValue(70.0);
        form.floor.setValue("ground floor");

        form.locationComponents.city.setItems(new City("Roma", "123"));
        form.locationComponents.city.setValue("123");

        form.locationComponents.address.setValue("Via Roma 1");

        var ad = new AdInsert();
        form.addFormValues(ad);

        assertEquals(70.0, ad.getDimensions());
        assertEquals(0, ad.getFloor());
        assertEquals("123", ad.getCity());  // expects the code
        assertEquals("Via Roma 1", ad.getAddress());
    }

}
