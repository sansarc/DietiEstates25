package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoIcon;

public class NotificationFactory {

    private NotificationFactory() {}

    public static void success(String text) {
        Notification.show(text, 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static void error(String text) {
        Notification.show(text, 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static void critical() {
        var notificationText = new TextWithLink("Error while reaching the server.", new Anchor("mailto:sansevieroarcangelo@gmail.com", "Contact us!"));
        notificationText.textSpan.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var notification = new Notification();
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(0);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);

        var closeButton = new Button(LumoIcon.CROSS.create(), event -> notification.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getStyle().setCursor("pointer");

        notification.add(notificationText, closeButton);
        notification.open();
    }
}
