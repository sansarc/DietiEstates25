package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.ui_components.InfoPopover;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.dom.Style;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class BadgeFactory {

    public static final String BADGE_CONTRAST = "badge contrast";

    private BadgeFactory() {}

    public static Span createTitle(String title) {
        var text = new Span(title);
        text.getStyle().setFontWeight(Style.FontWeight.BOLD).setCursor("default");
        return text;
    }

    public static Span doormanService() {
        var badge = new Span(createIcon(LineAwesomeIcon.USER_TIE_SOLID.create()), createTitle("Doorman Service"));
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        return badge;
    }

    public static Span condominiumParking() {
        var badge = new Span(createIcon(LineAwesomeIcon.PARKING_SOLID.create()), createTitle("Condominium Parking"));
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        return badge;
    }

    public static Span privateParking() {
        var badge = new Span(createIcon(LineAwesomeIcon.CAR_SOLID.create()), createTitle("Private Parking"));
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        return badge;
    }

    public static Span airConditioning() {
        var badge = new Span(createIcon(LineAwesomeIcon.SNOWFLAKE_SOLID.create()), createTitle("Air Conditioning"));
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        return badge;
    }

    public static Span squareMeters(int number) {
        var squareMeters = new Span("m²");
        squareMeters.getStyle().setFontSize("16px").setFontWeight(Style.FontWeight.BOLD);
        squareMeters.getStyle().setMarginLeft("4px");
        var badge = new Span(createTitle(String.valueOf(number)), squareMeters);
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        return badge;
    }

    public static Span rooms(int number) {
        var badge = new Span(createIcon(LineAwesomeIcon.COUCH_SOLID.create()), createTitle(number + (number > 1 ? " Rooms" : " Room")));
        badge.getElement().getThemeList().add("badge primary");
        return badge;
    }

    public static Span floor(int floor) {
        var badge = new Span(createIcon(LineAwesomeIcon.WALKING_SOLID.create()), createTitle("Floor " + floor));
        badge.getElement().getThemeList().add("badge primary");
        return badge;
    }

    public static Span bathrooms(int number) {
        var badge = new Span(createIcon(LineAwesomeIcon.BATH_SOLID.create()), createTitle(number + (number > 1 ? " Bathrooms" : " Bathroom")));
        badge.getElement().getThemeList().add("badge primary");
        return badge;
    }

    public static Span publicTransport() {
        var badge = new Span(createIcon(VaadinIcon.BUS.create()), createTitle("Public Transport"));
        badge.getElement().getThemeList().add(BADGE_CONTRAST);
        new InfoPopover(badge, "Public transport is available within 350m.").setPosition(PopoverPosition.BOTTOM);
        return badge;
    }

    public static Span school() {
        var badge = new Span(createIcon(LineAwesomeIcon.SCHOOL_SOLID.create()), createTitle("School"));
        badge.getElement().getThemeList().add("badge error");
        var popover = new InfoPopover(badge, "There's a school within 350m.");
        popover.setPosition(PopoverPosition.BOTTOM);
        return badge;
    }

    public static Span park() {
        var badge = new Span(createIcon(LineAwesomeIcon.TREE_SOLID.create()), createTitle("Park"));
        badge.getElement().getThemeList().add("badge success");
        var popover = new InfoPopover(badge, "There's a park within 350m.");
        popover.setPosition(PopoverPosition.BOTTOM);
        return badge;
    }

    private static Span createIcon(SvgIcon icon) {
        icon.getStyle()
                .setHeight("28px").setWidth("28px")
                .setPadding("var(--lumo-space-xs)");
        return new Span(icon);
    }

    private static Span createIcon(Icon icon) {
        icon.getStyle().setPadding("var(--lumo-space-xs)");
        return new Span(icon);
    }

}
