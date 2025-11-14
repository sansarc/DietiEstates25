package com.dieti.dietiestates25.views.mobile;

import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MobileComingSoonViewTest {

    private MobileComingSoonView view;

    @BeforeEach
    void setUp() {
        view = new MobileComingSoonView();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        assertEquals(3, view.getComponentCount());

        assertInstanceOf(DietiEstatesLogo.class, view.getComponentAt(0));
        assertInstanceOf(H1.class, view.getComponentAt(1));
        assertInstanceOf(Paragraph.class, view.getComponentAt(2));
    }
}