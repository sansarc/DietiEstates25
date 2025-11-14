package com.dieti.dietiestates25.views.mobile;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("coming-soon")
public class MobileComingSoonView extends VerticalLayout {
    H1 title = new H1("Coming Soon!");
    Paragraph message = new Paragraph(
            "Our mobile experience is under construction. " +
                    "Please visit our site on a desktop computer for the full experience."
    );

    public MobileComingSoonView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(title, message);
    }
}
