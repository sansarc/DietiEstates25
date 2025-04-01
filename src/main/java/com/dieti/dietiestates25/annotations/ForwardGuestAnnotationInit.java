package com.dieti.dietiestates25.annotations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

public class ForwardGuestAnnotationInit implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
           UI ui = uiInitEvent.getUI();
           ui.addBeforeEnterListener(beforeEnterEvent -> {
               Class<? extends Component> classTarget = (Class<? extends Component>) beforeEnterEvent.getNavigationTarget();
               if (classTarget.isAnnotationPresent(ForwardGuest.class)) {
                   if (VaadinSession.getCurrent().getAttribute("session_id") == null) {
                       ForwardGuest annotation = classTarget.getAnnotation(ForwardGuest.class);
                       Class<? extends Component> destination = annotation.value();
                       beforeEnterEvent.forwardTo(destination);
                   }
               }
           });
        });
    }

}
