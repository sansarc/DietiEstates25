package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class TextWithLink extends HorizontalLayout {

    public Span textSpan;

    public TextWithLink(String text, RouterLink link, String fontSize) {
        this.textSpan = new Span(text);
        link.getStyle().setMarginLeft("4px");

        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().setFontSize(fontSize);
        setSpacing(false);

        add(textSpan, link);
    }

    public TextWithLink(String text, Anchor link, String fontSize) {
        this.textSpan = new Span(text);
        link.getStyle().setMarginLeft("4px");

        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().setFontSize(fontSize);
        setSpacing(false);

        add(textSpan, link);
    }

    public TextWithLink(String text, RouterLink link) {
        this.textSpan = new Span(text);
        link.getStyle().setMarginLeft("4px");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(false);

        add(textSpan, link);
    }

    public TextWithLink(String text, Anchor link) {
        this.textSpan = new Span(text);
        link.getStyle().setMarginLeft("4px");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(false);

        add(textSpan, link);
    }

}
