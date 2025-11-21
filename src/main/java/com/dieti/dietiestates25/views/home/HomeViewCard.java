package com.dieti.dietiestates25.views.home;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

public class HomeViewCard extends Card {

    public HomeViewCard(String imagePath, String title, String description, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super();

        var cardImage = new Image(imagePath, "image");
        cardImage.setHeight("180px");
        cardImage.setWidth("200px");
        cardImage.getStyle().setMarginLeft("60px");
        setMedia(cardImage);

        setHeight("460px");
        setWidth("360px");

        addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED,
                CardVariant.LUMO_COVER_MEDIA
        );

        var header = new H2(title);
        header.getStyle().setMarginTop("20px");
        setTitle(header);

        var button = new Button(title, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().setMarginTop("10px").setCursor("pointer");

        var descriptionParagraph = new Paragraph(description);
        descriptionParagraph.getStyle()
                .setTextAlign(Style.TextAlign.CENTER)
                .setMarginTop("-10px");


        var verticalLayout = new VerticalLayout(descriptionParagraph, button);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);

        add(verticalLayout);

        getStyle()
                .setAlignItems(Style.AlignItems.CENTER)
                .setBorderRadius("var(--lumo-border-radius-l)")
                .setBoxShadow("var(--lumo-shadow-m)")
                .setPadding("var(--lumo-space-l)")
                .setTransition("transform 0.2s ease-in-out");


        getElement().addEventListener("mouseover", e -> getStyle().setTransform("scale(1.07)"));
        getElement().addEventListener("mouseout", e -> getStyle().setTransform("scale(1)"));
    }

}
