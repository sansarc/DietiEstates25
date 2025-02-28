package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.views.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;

public class DietiEstatesLogo extends Image {

    public DietiEstatesLogo() {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth("300px");
        setMaxHeight("150px");
        getStyle().setWidth("100%")
                .setHeight("auto")
                .setMarginBottom("var(--lumo-space-s)");
    }

    public DietiEstatesLogo(String maxWidth, String maxHeight) {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        getStyle().setWidth("100%")
                .setHeight("auto")
                .setMarginBottom("var(--lumo-space-s)");
    }

    public DietiEstatesLogo(boolean clickable) {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth("300px");
        setMaxHeight("150px");
        getStyle().setWidth("100%")
                .setHeight("auto")
                .setMarginBottom("var(--lumo-space-s)")
                .setCursor("pointer");

        addClickListener(event -> UI.getCurrent().navigate(HomeView.class));
    }

    public DietiEstatesLogo(String maxWidth, String maxHeight, boolean clickable) {
        super("/images/logo.png", "dietiestates_logo");

        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        getStyle().setWidth("100%")
                .setHeight("auto")
                .setMarginBottom("var(--lumo-space-s)")
                .setCursor("pointer");

        addClickListener(event -> UI.getCurrent().navigate(HomeView.class));
    }

}
