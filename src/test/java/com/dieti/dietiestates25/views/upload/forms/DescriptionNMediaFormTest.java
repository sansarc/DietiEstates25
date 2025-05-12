package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DescriptionNMediaFormTest {

    private DescriptionNMediaForm form;

    @BeforeEach
    void setUp() {
        form = new DescriptionNMediaForm();
    }

    @Test
    void testAddFormValuesNPhotos_setsCorrectAdValues() {
        form.price.setValue(100.0);
        form.description.setValue("This is a test ad");

        var ad = new AdInsert();

        form.addFormValuesNPhotos(ad, List.of(mock(Photo.class)));

        assertEquals(100.0, ad.getPrice());
        assertEquals("This is a test ad", ad.getDescription());
    }

}