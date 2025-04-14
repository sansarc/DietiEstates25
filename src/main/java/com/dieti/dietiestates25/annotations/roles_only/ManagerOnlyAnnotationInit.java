package com.dieti.dietiestates25.annotations.roles_only;

import com.dieti.dietiestates25.services.session.UserSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

public class ManagerOnlyAnnotationInit implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {

        event.getSource().addUIInitListener(uiInitEvent -> {
            UI ui = uiInitEvent.getUI();

            ui.addBeforeEnterListener(beforeEnterEvent -> {
                Class<? extends Component> classTarget = (Class<? extends Component>) beforeEnterEvent.getNavigationTarget();

                if (classTarget.isAnnotationPresent(ManagerOnly.class)) {
                    if (!UserSession.isManager()) {
                        ManagerOnly annotation = classTarget.getAnnotation(ManagerOnly.class);
                        Class<? extends Component> destination = annotation.value();
                        beforeEnterEvent.forwardTo(destination);
                    }
                }
            });
        });
    }
}
