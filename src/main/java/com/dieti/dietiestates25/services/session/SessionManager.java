package com.dieti.dietiestates25.services.session;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class SessionManager {

    private SessionManager() {}

    public static final long SESSION_TIMEOUT = 60L * 60 * 1000; // 1 hour
    public static final long WARNING_THRESHOLD = 10L * 60 * 1000; // 10 minutes

    private static boolean warned = false;

    public static void monitorSession(UI ui) {
        if (!UserSession.isUserLoggedIn()) return;

        ui.setPollInterval(120_000);
        ui.addPollListener(event -> checkSessionTimeout(ui));
    }

    private static void checkSessionTimeout(UI ui) {
        var start = UserSession.getSessionStart();
        if (start == null) return;

        long elapsed = System.currentTimeMillis() - start;
        long remaining = SESSION_TIMEOUT - elapsed;

        if (remaining <= WARNING_THRESHOLD && remaining > WARNING_THRESHOLD - 60_000 && !warned) {
            ui.access(() -> Notification.show("Your session will expire in 10 minutes.", 10000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING));
            warned = true;
        }

        if (remaining <= 0) {
            ui.access(() -> Notification.show("Session expired. Redirecting...", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING));
            UserSession.logout(true);
        }
    }

}
