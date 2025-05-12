package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdInsert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetailsFormTest {

    private DetailsForm form;

    @BeforeEach
    void setUp() {
        form = new DetailsForm();
    }

    @Test
    void addFormValues_setsCorrectAdValues() {
        form.nRooms.setValue(3);
        form.nBathrooms.setValue(2);
        form.elevator.setValue(true);
        form.energyClass.setValue("B");
        form.airConditioning.setValue(true);
        form.otherServices.select("Private garage", "Doorman service");

        var ad = new AdInsert();

        form.addFormValues(ad);

        assertEquals(3, ad.getNRooms());
        assertEquals(2, ad.getNBathrooms());
        assertTrue(ad.isElevator());
        assertEquals("B", ad.getEnergyClass());
        assertTrue(ad.isAC());

        assertTrue(ad.isPrivateGarage());
        assertFalse(ad.isCondominiumParking());
        assertTrue(ad.isDoormanService());
    }
}
