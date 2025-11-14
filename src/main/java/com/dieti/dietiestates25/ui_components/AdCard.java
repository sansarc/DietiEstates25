package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.ad.AdView;
import com.dieti.dietiestates25.views.search.SearchView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoIcon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdCard extends Card implements AfterNavigationObserver {

    private transient Ad ad;

    public AdCard(Ad ad) {
        this.ad = ad;

        setWidth("600px");
        setHeight("160px");
        getStyle().setCursor("pointer");

        addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED,
                CardVariant.LUMO_HORIZONTAL,
                CardVariant.LUMO_STRETCH_MEDIA
        );

        if (ad.getPhotos() != null && !ad.getPhotos().isEmpty()) {
            var image = ad.getPhotos().getFirst().toImage();
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

        String description;

        if (ad.getDescription() == null)
            description = "No description provided.";
        else if (ad.getDescription().length() > 47)
            description = ad.getDescription().substring(0, 46).trim() + "...";
        else
            description = ad.getDescription();

        add(new Span(description));

        if (ad.getAgent().getEmail().equals(UserSession.getEmail()) && UserSession.getCurrentPath().contains("profile")) {
            var trashButton = new Button("🗑", event -> {});
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
    }

    private void defineClickEvents(boolean isInSearchView) {
        if (isInSearchView) {  // on search is annoying to have this click event
            var goToAd = new Button(LumoIcon.ARROW_RIGHT.create(), event -> goToAd());
            goToAd.getStyle().setMarginLeft("190px");
            goToAd.setTooltipText("Go to ad");
            goToAd.getElement().addEventListener("mouseover", event -> goToAd.addThemeVariants(ButtonVariant.LUMO_PRIMARY));
            goToAd.getElement().addEventListener("mouseout", event -> goToAd.removeThemeVariants(ButtonVariant.LUMO_PRIMARY));
            goToAd.setDisableOnClick(true);
            adaptButtonToAdCard(goToAd);

            getStyle().setCursor("default");
            addToFooter(goToAd);
        }
        else {
            getStyle().setTransition("transform 0.2s ease-in-out");
            getElement().addEventListener("click", event -> goToAd());
            getElement().addEventListener("mouseover", event -> getStyle().setTransform("scale(1.07)"));
            getElement().addEventListener("mouseout", event -> getStyle().setTransform("scale(1)"));
        }
    }

    public static void adaptButtonToAdCard(Button goToAd) {
        goToAd.setHeight("80%");
        goToAd.setWidth("70px");
        goToAd.getStyle().setCursor("pointer").setMarginTop("16px");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        defineClickEvents(UI.getCurrent().getCurrentView() instanceof SearchView);
    }

    private void goToAd() {
        AdView.cacheAd(ad);
        UI.getCurrent().navigate(AdView.class, new RouteParameters("id", String.valueOf(ad.getId())));
    }
}
