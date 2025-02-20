package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;

public class ThirdPartyLoginButton extends Button {

    public ThirdPartyLoginButton(String label, String width, String iconPath, String url) {
        super("Sign up with " + label, event -> UI.getCurrent().getPage().setLocation(url));
        setWidth(width);

        Image logo = new Image(iconPath, "logo");
        logo.setHeight("16px");
        logo.setWidth("16px");

        setIcon(logo);
    }

}
