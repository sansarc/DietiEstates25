package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.utils.BadgeFactory;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

public class DivCard extends Div {

    public DivCard(String imageUrl, int price, String agencyImageUrl, String agencyName, String descriptionText) {
        getStyle()
                .setBorder("1px solid #e0e0e0")
                .setBorderRadius("8px")
                .setPadding("16px")
                .setWidth("360px")
                .setHeight("auto")
                .setMargin("8px")
                .setBoxShadow("0 2px 4px rgba(0, 0, 0, 0.1)")
                .setBackgroundColor("#F5F5F5")
                .setTransition("transform 0.4s ease-in-out   ");

        getElement().addEventListener("mouseover", event -> getStyle().setTransform("scale(1.07)"));
        getElement().addEventListener("mouseout", event -> getStyle().setTransform("scale(1)"));


        var image = new Image(imageUrl, "Photo");
        image.setWidth("100%");
        image.getStyle().setBorderRadius("8px 8px 0 0");

        var priceTitle = new Span("€ " + String.format("%,d", price));
        priceTitle.getStyle().setFontSize("20px").setFontWeight(Style.FontWeight.BOLD);

        var agency = new Span(BadgeFactory.createAgencyBadge(agencyImageUrl), new Span(agencyName));
        agency.getElement().getThemeList().add("badge contrast");

        var cardDescription = new Span(descriptionText);
        cardDescription.getStyle().setColor("#666").setFontSize("14px");

        var content = new VerticalLayout(priceTitle, agency, cardDescription);
        content.setSpacing(false);

        add(image, content);
    }
}
