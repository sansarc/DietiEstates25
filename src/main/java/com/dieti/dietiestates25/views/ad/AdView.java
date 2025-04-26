package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.ImagesCarousel;
import com.dieti.dietiestates25.ui_components.InteractiveMap;
import com.dieti.dietiestates25.utils.BadgeFactory;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.HashMap;
import java.util.Map;

@Route(value = "ad/:id", layout = MainLayout.class)
@PageTitle("Ad Details")
public class AdView extends VerticalLayout implements BeforeEnterObserver {

    AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    public static final String SCROLLER_CONTENT_WIDTH = "90%";

    ImagesCarousel imagesCarousel;
    VerticalLayout scrollerContent = new VerticalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    Scroller scroller = new Scroller(scrollerContent);

    H3 descriptionTitle;
    Span descriptionText;
    LDefaultComponentManagementRegistry registry;

    private static final Map<Integer, Ad> TEMP_AD_CACHE = new HashMap<>();

    public static void cacheAd(Ad ad) {
        TEMP_AD_CACHE.put(ad.getId(), ad);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var idParam = event.getRouteParameters().get("id");

        if (idParam.isPresent()) {
            var id = Integer.parseInt(idParam.get());
            var ad = new Ad();

            if (TEMP_AD_CACHE.containsKey(id)) {
                ad = TEMP_AD_CACHE.get(id);
                TEMP_AD_CACHE.remove(id);
            }
            else
                ad = adRequestsHandler.getAd(id);

            if (ad != null)
                configureComponents(ad);
            else
                event.forwardTo(HomeView.class);
        } else
            event.forwardTo(PageNotFoundView.class);
    }

    public AdView() {
        configureLayout();
    }

    private void configureComponents(Ad ad) {
        imagesCarousel = new ImagesCarousel(ad.getPhotos());

        createScrollerSide(ad);
//        createFixedSide(ad);

        layout.add(scroller, new BidsPanel(ad));
        layout.getStyle().setPosition(Style.Position.RELATIVE);

        add(imagesCarousel, layout);
    }

    public void createScrollerSide(Ad ad) {
        var descriptionDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "auto");
        descriptionTitle = new H3("About this property");
        descriptionText = ad.getDescription().isEmpty() ? new Span("No description given.") : new Span(ad.getDescription());

        var roomsBadgeLayout = new HorizontalLayout();
        roomsBadgeLayout.getStyle().setMarginTop("10px").setMarginBottom("10px");
        roomsBadgeLayout.add(BadgeFactory.rooms(ad.getNRooms()), BadgeFactory.bathrooms(ad.getNBathrooms()));

        descriptionDiv.add(descriptionTitle, new Hr(), new H3(ad.getPriceAsString()), roomsBadgeLayout, descriptionText);

        var mapDiv = new DivContainer(SCROLLER_CONTENT_WIDTH, "500px");
        var mapTitle = new H3("What's around");
        registry = new LDefaultComponentManagementRegistry(this);

        InteractiveMap map = null;

        if (ad.getCoordinates() != null)
            map = new InteractiveMap(registry, ad.getCoordinates());

        var badgeLayout = new HorizontalLayout();
        badgeLayout.getStyle().setMarginTop("10px").setMarginBottom("10px");
        if (ad.isSchool350m())
            badgeLayout.add(BadgeFactory.school());
        if (ad.isLeisurePark350m())
            badgeLayout.add(BadgeFactory.park());
        if (ad.isPublicTransport350m())
            badgeLayout.add(BadgeFactory.publicTransport());

        mapDiv.add(mapTitle, badgeLayout, map != null ? map : new Span("No coordinates were found for the given address."));
        scrollerContent.add(descriptionDiv, mapDiv);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        scroller.addClassName("no-scrollbar");
        scroller.setHeightFull();
        scroller.setWidth("70%");
        scroller.getStyle()
                .set("box-sizing", "border-box");

        scrollerContent.setWidthFull();

        layout.setWidth("100%");
        layout.setHeightFull();
    }
}
