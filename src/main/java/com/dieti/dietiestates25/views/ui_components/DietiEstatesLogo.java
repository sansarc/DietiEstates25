package com.dieti.dietiestates25.views.ui_components;

import com.vaadin.flow.component.html.Image;

public class DietiEstatesLogo extends Image {

    public DietiEstatesLogo() {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth("300px");
        setMaxHeight("150px");
        getStyle().set("width", "100%");
        getStyle().set("height", "auto");
        getStyle().set("margin-bottom", "var(--lumo-space-s)");
    }

    public DietiEstatesLogo(String maxWidth, String maxHeight) {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        getStyle().set("width", "100%");
        getStyle().set("height", "auto");
        getStyle().set("margin-bottom", "var(--lumo-space-s)");
    }

}
