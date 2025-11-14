package com.dieti.dietiestates25.views.mobile;

import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("coming-soon")
public class MobileComingSoonView extends VerticalLayout {
    public MobileComingSoonView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                new DietiEstatesLogo("auto", "150px", false),
                new H1("Coming Soon!"),
                new Paragraph("We're working on it! Stay tuned.")
        );
    }
}
