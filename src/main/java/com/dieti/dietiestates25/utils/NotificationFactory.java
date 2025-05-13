package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoIcon;

public class NotificationFactory {

    private NotificationFactory() {}

    public static void success(String text) {
        UI.getCurrent().access(() -> Notification.show(text, 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS));
    }

    public static Notification error(String text) {
        var notification = new Notification(text, 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
        return notification;
    }

    public static void criticalError(String exceptionText) {
        var errorText = new Span("Critical Error:");
        errorText.getStyle().setMarginRight("4px").setFontWeight(Style.FontWeight.BOLD);
        var notificationText = new VerticalLayout(new Span(errorText), new Span(exceptionText), new Anchor("mailto:sansevieroarcangelo@gmail.com", "Let us know!"));
        styleNotificationNOpen(notificationText);
    }

    private static void styleNotificationNOpen(VerticalLayout notificationText) {
        var notification = new Notification();
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(0); // persistent
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);

        var layout = new HorizontalLayout(notificationText, getCloseButtonFor(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);

        notification.add(layout);

        UI.getCurrent().access(notification::open);
    }

    private static Button getCloseButtonFor(Notification notification) {
        var closeButton = new Button(LumoIcon.CROSS.create(), event -> notification.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getStyle().setCursor("pointer");
        return closeButton;
    }

    public static void primary(String text) {
        UI.getCurrent().access(() -> {
            var notification = new Notification();
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setDuration(8000);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);

            notification.add(new Span(text), getCloseButtonFor(notification));
            notification.open();
        });
    }

}
