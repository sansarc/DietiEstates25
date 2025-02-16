package com.dieti.dietiestates25.views.ui_components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class TextWithLink extends HorizontalLayout {

    public TextWithLink(String text, RouterLink link) {
        super(new Span(text), link);

        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("font-size", "14px");
        link.getStyle().set("margin-left", "4px");
        setSpacing(false);
    }

}
