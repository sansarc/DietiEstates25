package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.ad.AdView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoIcon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdCard extends Card {


    public AdCard(Ad ad) {
        setWidth("600px");
        setHeight("150px");
        getStyle().setCursor("pointer");

        addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED,
                CardVariant.LUMO_HORIZONTAL,
                CardVariant.LUMO_STRETCH_MEDIA
        );

        if (ad.getPhotos() != null && !ad.getPhotos().isEmpty()) {
            var image = ad.getPhotos().get(0).toImage();
            image.setWidth("200px");
            setMedia(image);
        }
        else {
            var icon = LumoIcon.PHOTO.create();
            icon.getStyle()
                    .setWidth("200px")
                    .setColor(Constants.Colors.PRIMARY_BLUE)
                    .setBackgroundColor("var(--lumo-primary-color-10pct)");
            setMedia(icon);
            new InfoPopover(getMedia(), "No images were provided.");
        }

        setTitle(String.format("%.2f", ad.getPrice()) + "€");
        setSubtitle(new Div(ad.getNRooms() + " rooms | " + ad.getNBathrooms() + " bathrooms"));
        var agencyBadge = new Span(ad.getAgent().getAgencyName());
        agencyBadge.getElement().getThemeList().add("badge contrast");
        var typeBadge = new Span(ad.getType().equals("R") ? "Renting" : "Selling");
        typeBadge.getElement().getThemeList().add("badge primary");
        var badgesLayout = new VerticalLayout(agencyBadge, typeBadge);
        badgesLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        badgesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        badgesLayout.setPadding(false);
        badgesLayout.setSpacing(false);
        badgesLayout.setMargin(false);
        setHeaderSuffix(badgesLayout);

        add(ad.getDescription() == null
                ? new Span("No description provided.")
                : new Span(ad.getDescription().length() > 47
                    ? ad.getDescription().substring(0, 47).trim() + "..."
                    : ad.getDescription()
                )
        );

        if (ad.getAgent().getEmail().equals(UserSession.getEmail()) && UserSession.getCurrentPath().contains("profile")) {
            var trashButton = new Button("🗑", event -> {});
            trashButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            trashButton.setTooltipText("Delete");
            trashButton.getStyle().setCursor("pointer");

            var footer = new HorizontalLayout(trashButton);
            footer.setWidthFull();
            footer.setSpacing(false);
            footer.setMargin(false);
            footer.setPadding(false);
            footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            addToFooter(footer);
        }


        if (UserSession.getCurrentPath().contains("search")) {  // on search is annoying to have this click event
            var goToAd = new Anchor(String.valueOf(ad.getId()), "Go to Ad");
            goToAd.getElement().addEventListener("click", event -> goToAd(ad))
                            .addEventData("preventDefault()");
            addToFooter(goToAd);
        }
        else {
            getStyle().setTransition("transform 0.2s ease-in-out");
            getElement().addEventListener("click", event -> goToAd(ad));
            getElement().addEventListener("mouseover", event -> getStyle().setTransform("scale(1.07)"));
            getElement().addEventListener("mouseout", event -> getStyle().setTransform("scale(1)"));
        }
    }

    private void goToAd(Ad ad) {
        AdView.cacheAd(ad);
        UI.getCurrent().navigate(AdView.class, new RouteParameters("id", String.valueOf(ad.getId())));
    }
}
