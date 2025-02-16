package com.dieti.dietiestates25.views.ui_components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;

public class CustomInfoPopover extends Popover {

    public CustomInfoPopover(Component target, final String POPOVER_TEXT) {
        configurePopover();
        setContent(POPOVER_TEXT);
        setTarget(target);
    }

    private void configurePopover() {
        setPosition(PopoverPosition.END);
        setOpenOnClick(true);
        setOpenOnFocus(true);
        setOpenOnHover(true);
    }

    private void setContent(String POPOVER_TEXT) {
        var content = new Div(new Text(POPOVER_TEXT));
        content.getStyle()
                .set("max-width", "200px")
                .set("font-size", "12px")
                .set("color", "var(--lumo-secondary-text-color)");
        add(content);
    }
}