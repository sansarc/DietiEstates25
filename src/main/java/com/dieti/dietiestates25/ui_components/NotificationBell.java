package com.dieti.dietiestates25.ui_components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.theme.lumo.LumoIcon;

public class NotificationBell extends Button {

    public NotificationBell() {
        setIcon(LumoIcon.BELL.create());
        addClickListener(e -> {
            Notification.show("No new notification.", 5000, Notification.Position.TOP_CENTER);
        });
    }

}
