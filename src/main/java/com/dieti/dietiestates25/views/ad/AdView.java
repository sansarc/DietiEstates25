package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.ui_components.Map;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

@Route(value = "ad", layout = MainLayout.class)
public class AdView extends VerticalLayout {

    public static final String SCROLLER_CONTENT_WIDTH = "90%";
    HorizontalLayout picturesLayout = new HorizontalLayout();
    VerticalLayout scrollerContent = new VerticalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    Scroller scroller = new Scroller(scrollerContent);
    Div bidDiv = new DivContainer("30%", "400px");

    public AdView() {
        configureLayout();
        configureComponents();
    }

    private void configureComponents() {
        var photos = new Image("https://picsum.photos/2670/1780", "photo");
        photos.setWidth("60%");
        photos.setHeight("80%");
        var leftButton = new Button(VaadinIcon.ARROW_LEFT.create());
        var rightButton = new Button(VaadinIcon.ARROW_RIGHT.create());
        picturesLayout.add(leftButton, photos, rightButton);

        var descriptionDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "300px");
        var descriptionTitle = new H3("About this home");
        descriptionDiv.add(descriptionTitle);
        var mapDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "500px");
        var mapTitle = new H3("What's around this home");
        final var registry = new LDefaultComponentManagementRegistry(this);
        final var map = new Map(registry, 52.5200, 13.4050, 12); // Berlin
        map.addMarker(registry, 52.5200, 13.4050, "Berlin");
        mapDiv.add(mapTitle, map);

        var agentTitle = new H3("About the agent");
        var name = "Agent Smith";
        var agentAvatar = new Avatar(name);
        agentAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        agentAvatar.getStyle().set("border-color", Constants.Colors.PRIMARY_BLUE);
        agentAvatar.getStyle().setCursor("pointer");
        var agentName = new Anchor("example.com", name);
        agentName.getStyle().setFontWeight(Style.FontWeight.BOLD);
        var agentLayout = new HorizontalLayout(agentAvatar, agentName);
        agentLayout.setAlignItems(Alignment.CENTER);
        agentLayout.getStyle().setMarginTop("20px").setCursor("pointer");
        var bidTitle = new H4("Make an offer for this listing");
        bidTitle.getStyle().setMarginBottom("10px");
        var bidTextField = Form.priceInEuroNumberField("", false);
        bidTextField.getStyle().setMarginBottom("4px");
        var bidSend = new Button("Send");
        bidSend.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bidDiv.add(agentTitle, agentLayout, new Hr(), bidTitle, bidTextField, bidSend);

        scrollerContent.add(descriptionDiv, mapDiv);
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
        scroller.getStyle().setMarginTop("-15px");
        scrollerContent.setWidthFull();

        bidDiv.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setRight("0")
                .setTop("0")
                .setTransition("transform 0.1s").setTransform("translateX(8%)");

        layout.setWidth("90%");
        layout.setHeightFull();
    }
}
