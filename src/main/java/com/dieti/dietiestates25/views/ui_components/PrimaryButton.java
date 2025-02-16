package com.dieti.dietiestates25.views.ui_components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class PrimaryButton extends Button {

    public PrimaryButton(String label, String width) {

        super(label);

        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setWidth(width);
    }

}
