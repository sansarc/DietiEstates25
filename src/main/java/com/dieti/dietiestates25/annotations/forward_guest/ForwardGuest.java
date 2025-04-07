package com.dieti.dietiestates25.annotations.forward_guest;

import com.dieti.dietiestates25.views.home.HomeView;
import com.vaadin.flow.component.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ForwardGuest {
    Class<? extends Component> value() default HomeView.class;
}
