package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;

public class ResendLink extends Span {

    Span resendLink;
    Span countdownText;
    int COUNTDOWN = 30;
    Registration clickListenerRegistration;
    Registration pollListenerRegistration = () -> {}; // prevents NPE
    UI ui;
    Runnable action;

    public ResendLink(Runnable action) {
        this.action = action;
        ui = UI.getCurrent();

        resendLink = new Span("Resend code");
        resendLink.getStyle()
                .setColor("lightgray")
                .setTextDecoration("underline")
                .setCursor("pointer")
                .setMarginRight("4px");

        countdownText = new Span(" in " + COUNTDOWN + " seconds");

        add(resendLink, countdownText);

        disableResendLink();
        startCountdown();
    }

    private void startCountdown() {
        ui.access(() -> {
            ui.setPollInterval(1000);
            pollListenerRegistration.remove();

            pollListenerRegistration = ui.addPollListener(event -> {
                if (COUNTDOWN > 0) {
                    COUNTDOWN--;
                    countdownText.setText(" in " + COUNTDOWN + " seconds");
                } else {
                    enableResendLink();
                    ui.setPollInterval(-1);
                    pollListenerRegistration.remove();
                }
            });
        });
    }

    private void enableResendLink() {
        resendLink.getStyle().setColor(Constants.Colors.PRIMARY_BLUE);
        resendLink.getStyle().setCursor("pointer");

        clickListenerRegistration = resendLink.addClickListener(event -> {
            action.run();
            disableResendLink();
            COUNTDOWN = 30;
            startCountdown();
        });
    }

    private void disableResendLink() {
        resendLink.getStyle().setColor("lightgray");
        resendLink.getStyle().setCursor("not-allowed");

        if (clickListenerRegistration != null) {
            clickListenerRegistration.remove();
        }
    }
}
