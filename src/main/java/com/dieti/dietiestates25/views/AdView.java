package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.ui_components.DivContainer;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route(value = "ad", layout = MainLayout.class)
public class AdView extends VerticalLayout {

    public static final String SCROLLER_CONTENT_WIDTH = "90%";
    HorizontalLayout picturesLayout = new HorizontalLayout();
    VerticalLayout scrollerContent = new VerticalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    Scroller scroller = new Scroller(scrollerContent);

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

        var generalInfoDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "200px");
        var descriptionDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "400px");
        var descriptionTitle = new H3("About this home");
        descriptionDiv.add(descriptionTitle);
        var mapDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "900px");
        var mapTitle = new H3("What's around this home");
        mapDiv.add(mapTitle);

        var bidDiv = new DivContainer("30%", "400px");
        bidDiv.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setRight("0")
                .setTop("0")
                .setTransition("transform 0.1s");
        var bidTitle = new H3("About the agent");
        var agentAvatar = new Avatar();
        bidDiv.add(bidTitle);

        scrollerContent.add(generalInfoDiv, descriptionDiv, mapDiv);
        layout.add(scroller, bidDiv);
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

        scroller.setHeightFull();
        scroller.setWidth("70%");
        scrollerContent.setWidthFull();

        layout.setWidth("70%");
        layout.setHeightFull();
    }
}
