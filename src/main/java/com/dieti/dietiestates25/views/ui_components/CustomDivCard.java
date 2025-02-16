package com.dieti.dietiestates25.views.ui_components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CustomDivCard extends Div {

    public CustomDivCard(String width, String height) {
        addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE
        );

        setWidth(width);
        setHeight(height);

        getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "var(--lumo-space-xs)");
    }
}
