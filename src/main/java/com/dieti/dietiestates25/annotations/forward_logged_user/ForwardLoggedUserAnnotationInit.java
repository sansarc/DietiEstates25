package com.dieti.dietiestates25.annotations.forward_logged_user;

import com.dieti.dietiestates25.services.session.UserSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

public class ForwardLoggedUserAnnotationInit implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
            UI ui = uiInitEvent.getUI();
            ui.addBeforeEnterListener(beforeEnterEvent -> {
                Class<? extends Component> classTarget = (Class<? extends Component>) beforeEnterEvent.getNavigationTarget();
                if (classTarget.isAnnotationPresent(ForwardLoggedUser.class)) {
                    if (UserSession.isUserLoggedIn()) {
                        ForwardLoggedUser annotation = classTarget.getAnnotation(ForwardLoggedUser.class);
                        Class<? extends Component> destination = annotation.value();
                        beforeEnterEvent.forwardTo(destination);
                    }
                }
            });
        });
    }

}
