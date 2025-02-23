package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.html.Image;

public class BadgeFactory {

    private BadgeFactory() {}

    public static Image createAgencyBadge(String imageUrl) {
        var image = new Image(imageUrl, "agency_badge");
        image.setWidth("20px");
        image.setHeight("20px");
        image.getStyle().setPaddingRight("8px");

        return image;
    }

}
