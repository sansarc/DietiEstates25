package com.dieti.dietiestates25.views.profile;

import com.dieti.dietiestates25.dto.Agency;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.observers.BidActionListener;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.AdCard;
import com.dieti.dietiestates25.ui_components.BidMessage;
import com.dieti.dietiestates25.views.agency_dashboard.AgencyDashboardView;
import com.dieti.dietiestates25.annotations.forward_guest.ForwardGuest;
import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;

@ForwardGuest(LoginView.class)
@Route(value = "profile", layout = MainLayout.class)
@RouteAlias(value = "profile/:email", layout = MainLayout.class)
public class Profile extends VerticalLayout implements BeforeEnterObserver, BidActionListener {

    private final AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    private static final Map<String, User> TEMP_USER_CACHE = new HashMap<>();

    DivContainer container;
    VerticalLayout bidsListLayout;

    public static void cacheUser(User user) {
        TEMP_USER_CACHE.put(user.getEmail(), user);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var emailParam = event.getRouteParameters().get("email");
        User user;

        if (emailParam.isPresent() && !emailParam.get().equals(UserSession.getEmail())) {  // practically, if the user's clicking on its own profile, send them to /profile
            var email = emailParam.get();

            if (TEMP_USER_CACHE.containsKey(email)) {
                user = TEMP_USER_CACHE.get(email);
                configureComponents(user, false);
            } else {
                event.forwardTo(PageNotFoundView.class);
                return;
            }
        } else { // no email parameter -> user visiting their own profile
            user = UserSession.asUser();
            configureComponents(user, true);
        }

        configureLayout();

        UI.getCurrent().access(() ->
                UI.getCurrent().getPage().setTitle(user.getFirstName() + "'s Profile")
        );
    }

    public Profile() {}

    private void configureComponents(User user, boolean isPersonalProfile) {
        removeAll(); // to prevent duplication

        container = new DivContainer("600px", "180px");
        container.getStyle().setMarginBottom("-10px");

        var fullName = user.getFirstName() + " " + user.getLastName();
        var avatar = new Avatar(fullName);
        avatar.setWidth("100px");
        avatar.setHeight("100px");
        avatar.getStyle()
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE)
                .setMarginTop("-10px").setMarginBottom("var(--lumo-space-xs)");

        var name = new H2(fullName);
        var email = new Anchor("mailto:" + user.getEmail(), user.getEmail());
        email.getElement().getThemeList().add("h5");
        email.getStyle().set("color", "#888888");

        container.add(avatar, name, email);
        add(container);

        System.out.println(user.getRole());

        if ("A".equals(user.getRole()) || "M".equals(user.getRole())) {
            RouterLink agencyLink;
            if (isPersonalProfile)
                agencyLink = new RouterLink(user.getAgencyName(), AgencyDashboardView.class);
            else {
                agencyLink = new RouterLink(user.getAgencyName(), AgencyDashboardView.class, new RouteParameters("agency", user.getAgencyName()));
                agencyLink.getElement().addEventListener("click", event -> AgencyDashboardView.cacheAgency(new Agency(user.getAgencyName(), user.getAgencyVAT())));
            }

            var agency = new TextWithLink("Agency: ", agencyLink);
            container.add(agency);

            var adsTitle = new H3("Ads");
            adsTitle.getStyle().setPaddingTop("var(--lumo-space-m)");
            add(adsTitle);

            var ads = adRequestsHandler.getAdsByAgent(user.getEmail());
            var adList = new VerticalLayout();
            for (var ad : ads) {
                var adCard = new AdCard(ad);
                adList.add(adCard);
            }

            adList.setAlignItems(Alignment.CENTER);
            var scroller = new Scroller(adList);
            scroller.setWidth("70%");
            scroller.setHeight("auto");
            scroller.setMaxHeight("300px");
            scroller.getStyle()
                    .setBorder("0.5px solid #ccc")
                    .setBorderRadius("4px")
                    .setBackgroundColor("#f9f9f9");
            add(scroller);
        }

        if (isPersonalProfile) {
            bidsListLayout = new VerticalLayout();
            bidsListLayout.removeAll();

            var bidsTitle = new H3("Bids you've placed.");
            bidsTitle.getStyle().setPaddingTop("var(--lumo-space-m)");
            add(bidsTitle);

            var bids = adRequestsHandler.getBidsBy("offerer", user.getEmail());

            for (var bid : bids) {
                if (!bid.getStatus().equals("C")) {
                    var bidMessage = new BidMessage(bid, "", this);

                    if (bid.getStatus().equals("A")) {
                        if (bid.getCounteroffer() == null)
                            bidMessage.setAccepted();
                        bidsListLayout.addComponentAsFirst(bidMessage);
                    } else if (bid.getStatus().equals("R")) {
                        bidMessage.setRefused();
                        bidsListLayout.add(bidMessage);
                    } else bidsListLayout.add(bidMessage);
                }
            }

            if (bidsListLayout.getComponentCount() > 0) {
                var scroller = new Scroller(bidsListLayout);
                scroller.setWidth("60%");
                scroller.setHeight("auto");
                scroller.setMaxHeight("400px");
                scroller.getStyle()
                        .setBorder("0.5px solid #ccc")
                        .setBorderRadius("4px")
                        .setBackgroundColor("#f9f9f9");
                add(scroller);
            } else
                add(new Span("Looks like you haven't place a bid yet."));
        }
    }

    @Override public void onAccepted(Bid bid) {}
    @Override public void onRefused(Bid bid) {}

    @Override
    public void onDeleted(Bid bid) {
        bidsListLayout.remove(BidMessage.find(bidsListLayout, bid.getId()));
    }

    private void configureLayout() {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.CENTER);

        container.getStyle().setAlignItems(Style.AlignItems.CENTER);
    }

}

