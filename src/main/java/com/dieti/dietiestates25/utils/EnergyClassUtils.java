package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import lombok.Getter;

public class EnergyClassUtils {

    public static void setRenderer(Select<String> energyClass) {
        energyClass.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item);

            try {
                // Only apply styling for valid enum values
                EnergyClass ec = EnergyClass.valueOf(item);

                span.getStyle()
                        .setBackground(ec.getColor())
                        .setPadding("4px 8px")
                        .setBorderRadius("4px")
                        .setFontWeight(Style.FontWeight.BOLD);

                if (item.equals("E") || item.equals("F") || item.equals("G"))
                    span.getStyle().setColor("white");
                else
                    span.getStyle().setColor("black");
            } catch (IllegalArgumentException e) {
                // Do nothing for non-enum values - no styling applied
            }

            return span;
        }));
    }

    @Getter
    private enum EnergyClass {
        A("green"), B("lightgreen"), C("yellow"), D("orange"), E("red"), F("darkred"), G("maroon");

        private final String color;

        EnergyClass(String color) {
            this.color = color;
        }
    }
}
