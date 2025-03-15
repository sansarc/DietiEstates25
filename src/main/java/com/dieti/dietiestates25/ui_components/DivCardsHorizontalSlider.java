package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

import java.util.List;

public class DivCardsHorizontalSlider extends Div {

    HorizontalLayout cardContainer;
    List<DivCard> cards;
    Div wrapper;
    private int currentIndex = 0;
    private final int CARD_WIDTH;
    private final int VISIBLE_CARDS = 3;

    public DivCardsHorizontalSlider(List<DivCard> cardList) {
        cards = cardList;
        CARD_WIDTH = Integer.parseInt(cards.get(0).getWidth().replace("px", ""));

        setWidthFull();
        getStyle()
                .setDisplay(Style.Display.FLEX)
                .setFlexDirection(Style.FlexDirection.COLUMN)
                .setAlignItems(Style.AlignItems.CENTER);

        cardContainer = new HorizontalLayout();
        for (DivCard card : cards) {
            card.getStyle().setMarginTop("30px").setMarginBottom("30px");
            cardContainer.add(card);
        }

        cardContainer.setWidth((cards.size() * CARD_WIDTH) + "px");
        cardContainer.setSpacing(false);
        cardContainer.getStyle().setTransition("transform 0.5s ease");

        wrapper = new Div(cardContainer);
        wrapper.setWidth((VISIBLE_CARDS * CARD_WIDTH) + "px");
        wrapper.setHeight("425px");
        wrapper.getStyle().setPaddingLeft("5px").setPaddingRight("5px");    // DivCard animation size adjustments
        wrapper.getStyle().setOverflow(Style.Overflow.HIDDEN);

        Button prevButton = new Button("", VaadinIcon.ARROW_LEFT.create());
        prevButton.addClickListener(event -> showPreviousCards());
        prevButton.getStyle().setMarginRight("8px");
        prevButton.addClickShortcut(Key.ARROW_LEFT);

        Button nextButton = new Button("", VaadinIcon.ARROW_RIGHT.create());
        nextButton.addClickListener(event -> showNextCards());
        nextButton.getStyle().setMarginLeft("8px");
        nextButton.addClickShortcut(Key.ARROW_RIGHT);

        add(wrapper, new HorizontalLayout(prevButton, nextButton));
    }

    private void updateVisibleCards() {
        int offset = currentIndex * CARD_WIDTH;
        cardContainer.getStyle().set("transform", "translateX(-" + offset + "px)");
    }

    private void showPreviousCards() {
        if (currentIndex > 0) {
            currentIndex = Math.max(0, currentIndex - VISIBLE_CARDS);
            updateVisibleCards();
        }
    }

    private void showNextCards() {
        if (currentIndex + VISIBLE_CARDS < cards.size()) {
            currentIndex += VISIBLE_CARDS;
            updateVisibleCards();
        }
    }

    public String getCardsWrapperActualWidth() {
        return wrapper.getWidth();
    }
}