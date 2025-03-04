package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.ui_components.DivContainer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "ad", layout = MainLayout.class)
public class AdView extends VerticalLayout {

    HorizontalLayout picturesLayout = new HorizontalLayout();
    VerticalLayout leftLayout = new VerticalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    Scroller leftScroller = new Scroller(leftLayout);

    public AdView() {
        configureLayout();
        configureComponents();
        // addScrollTracking();
    }

    private void configureComponents() {
        var photos = new Image("https://picsum.photos/2670/1780", "photo");
        photos.setWidth("60%");
        photos.setHeight("80%");
        var leftButton = new Button(VaadinIcon.ARROW_LEFT.create());
        var rightButton = new Button(VaadinIcon.ARROW_RIGHT.create());
        picturesLayout.add(leftButton, photos, rightButton);

        var generalInfoDiv = new DivContainer("90%", "200px");
        var descriptionDiv = new DivContainer("90%", "400px");
        var descriptionTitle = new H3("About this home");
        descriptionDiv.add(descriptionTitle);
        var mapDiv = new DivContainer("90%", "900px");
        var mapTitle = new H3("What's around this home");
        mapDiv.add(mapTitle);

        var bidDiv = new DivContainer("40%", "400px");
        bidDiv.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setRight("0")
                .setTop("0")
                .setTransition("transform 0.1s");

        leftLayout.add(generalInfoDiv, descriptionDiv, mapDiv);
        layout.add(leftScroller, bidDiv);
        layout.getStyle().setPosition(Style.Position.RELATIVE);

        add(picturesLayout, layout);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setSpacing(false);
        setPadding(false);

        picturesLayout.setWidthFull();
        picturesLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        picturesLayout.setAlignItems(Alignment.CENTER);

        leftLayout.setSizeFull();
        layout.setSizeFull();

        leftScroller.setSizeFull();
    }
}
