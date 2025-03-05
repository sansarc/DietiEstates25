package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.geoapify.GeoapifyService;
import com.dieti.dietiestates25.ui_components.DivContainer;
import software.xdev.vaadin.maps.leaflet.map.*;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "ad", layout = MainLayout.class)
public class AdView extends VerticalLayout {

    @Autowired
    private GeoapifyService geoapifyService;

    public static final String SCROLLER_CONTENT_WIDTH = "90%";
    HorizontalLayout picturesLayout = new HorizontalLayout();
    VerticalLayout scrollerContent = new VerticalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    Scroller scroller = new Scroller(scrollerContent);

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

        var generalInfoDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "200px");
        var descriptionDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "400px");
        var descriptionTitle = new H3("About this home");
        descriptionDiv.add(descriptionTitle);
        var mapDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "900px");
        var mapTitle = new H3("What's around this home");
        var map = new Div();
        mapDiv.add(mapTitle, map);

        var bidDiv = new DivContainer("30%", "400px");
        bidDiv.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setRight("0")
                .setTop("0")
                .setTransition("transform 0.1s");
        var bidTitle = new H3("Set your price");
        bidTitle.getStyle().setPaddingBottom("20px");
        var agentAvatar = new Avatar("John Doe");
        agentAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        agentAvatar.getStyle()
                .setCursor("pointer")
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE);
        agentAvatar.getAbbreviation();
        var agentName = new H4("John Doe");
        agentName.getStyle().setCursor("pointer");
        var agentProfileLayout = new HorizontalLayout(agentAvatar, agentName);
        agentProfileLayout.setAlignItems(Alignment.CENTER);
        agentProfileLayout.getStyle().setPaddingBottom("30px");
        var bidTextField = new NumberField("Your offer", "");
        bidTextField.setSuffixComponent(VaadinIcon.EURO.create());
        bidTextField.getStyle()
                .set("--vaadin-input-field-height", "30px")
                .set("--vaadin-input-field-value-font-size", "20px")
                .set("--vaadin-input-field-value-font-weight", LumoUtility.FontWeight.BOLD);
        var confirmBid = new Button("Confirm");
        confirmBid.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bidDiv.add(bidTitle, agentProfileLayout, bidTextField, confirmBid);

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
        scroller.setWidth("65%");
        scrollerContent.setWidthFull();

        layout.setWidth("90%");
        layout.setHeightFull();
    }
}
