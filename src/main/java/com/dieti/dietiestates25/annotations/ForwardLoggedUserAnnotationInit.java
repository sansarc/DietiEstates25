package com.dieti.dietiestates25.annotations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

public class ForwardLoggedUserAnnotationInit implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
            UI ui = uiInitEvent.getUI();
            ui.addBeforeEnterListener(beforeEnterEvent -> {
                Class<?> classTarget = beforeEnterEvent.getNavigationTarget();
                if (classTarget.isAnnotationPresent(ForwardLoggedUser.class)) {
                    if (VaadinSession.getCurrent().getAttribute("session_id") != null) {
                        ForwardLoggedUser annotation = classTarget.getAnnotation(ForwardLoggedUser.class);
                        Class<? extends Component> destination = annotation.value();
                        System.out.println("@ForwardLoggedUser: redirecting to " + destination);
                        beforeEnterEvent.forwardTo(destination);
                    }
                }
            });
        });
    }

}
