package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.views.upload.forms.DetailsForm;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import lombok.Getter;

public class EnergyClassUtils {

    public static void setRenderer(Select<String> energyClass) {
        energyClass.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item);
            String bgColor = getEnergyClassColor(item);

            span.getStyle()
                    .setBackground(bgColor)
                    .setPadding("4px 8px")
                    .setBorderRadius("4px")
                    .setFontWeight(Style.FontWeight.BOLD);

            if (item.equals("E") || item.equals("F") || item.equals("G")) {
                span.getStyle().setColor("white");
            } else {
                span.getStyle().setColor("black");
            }

            return span;
        }));
    }

    private static String getEnergyClassColor(String item) {
        try {
            return EnergyClass.valueOf(item).getColor();
        } catch (IllegalArgumentException e) {
            return "black";
        }
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


