package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.observers.ThemeChangeListener;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;
import com.dieti.dietiestates25.utils.BadgeFactory;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.VaadinSession;

public class DivCard extends Div implements ThemeChangeListener {

    public DivCard(String imageUrl, int price, String agencyImageUrl, String agencyName, String descriptionText) {

        ThemeChangeNotifier.addListener(this);
        addDetachListener(event -> ThemeChangeNotifier.removeListener(this)); // memory leaks
        Boolean darkTheme = (Boolean) VaadinSession.getCurrent().getAttribute("darkTheme");
        applyTheme(darkTheme != null ? darkTheme : false);

        configureStyle();
        configureComponents(imageUrl, price, agencyImageUrl, agencyName, descriptionText);
    }

    private void configureComponents(String imageUrl, int price, String agencyImageUrl, String agencyName, String descriptionText) {
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

    private void configureStyle() {
        getStyle()
                .setBorder("1px solid #e0e0e0")
                .setBorderRadius("8px")
                .setPadding("16px")
                .setWidth("360px")
                .setHeight("auto")
                .setMargin("8px")
                .setBoxShadow("0 2px 4px rgba(0, 0, 0, 0.1)")
                .setTransition("transform 0.4s ease-in-out");

        getElement().addEventListener("mouseover", event -> getStyle().setTransform("scale(1.07)"));
        getElement().addEventListener("mouseout", event -> getStyle().setTransform("scale(1)"));
    }

    @Override
    public void onThemeChange(boolean darkTheme) {
        applyTheme(darkTheme);
    }

    private void applyTheme(boolean darkTheme) {
        String backgroundColor = darkTheme
                ? Constants.Colors.GRAY_OVER_DARKMODE
                : Constants.Colors.GRAY_OVER_WHITEMODE;
        getStyle().setBackgroundColor(backgroundColor).set("border-color", backgroundColor);
    }


}
