package com.dieti.dietiestates25.views.profile;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.views.agency_dashboard.AgencyDashboardView;
import com.dieti.dietiestates25.annotations.forward_guest.ForwardGuest;
import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;

@ForwardGuest
@Route(value = "profile", layout = MainLayout.class)
@RouteAlias(value = "profile/:email", layout = MainLayout.class)
public class Profile extends VerticalLayout implements BeforeEnterObserver {

    private static final Map<String, User> TEMP_USER_CACHE = new HashMap<>();

    DivContainer container;

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
            }
            else {
                event.forwardTo(PageNotFoundView.class);
                return;
            }
        }
        else { // no email parameter -> user visiting their own profile
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

        container = new DivContainer("600px", "auto");

        var fullName = user.getFirstName() + " " + user.getLastName();
        var avatar = new Avatar(fullName);
        avatar.setWidth("100px");
        avatar.setHeight("100px");
        avatar.getStyle()
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE)
                .setMarginTop("var(--lumo-space-m)").setMarginBottom("var(--lumo-space-m)");

        var name = new H1(fullName);
        var email = new Anchor("mailto:" + user.getEmail(), user.getEmail());
        email.getStyle().set("color", "#888888").setFontSize("20px").setFontWeight(Style.FontWeight.BOLD);

        container.add(avatar, name, email);

        add(container);

        if (user.getRole().equals("A") || user.getRole().equals("M")) {
            var agency = new TextWithLink("Agency: ", new RouterLink(user.getAgency(), AgencyDashboardView.class));
            container.add(agency);

            var adsTitle = new H2("Ads");
            adsTitle.getStyle().setPaddingTop("var(--lumo-space-m)");
            add(adsTitle);
        }

        if (isPersonalProfile) {
            var bidsTitle = new H2("Your Bids");
            bidsTitle.getStyle().setPaddingTop("var(--lumo-space-m)");
            var bids = new Span("Looks like you haven't place a bid yet.");
            bids.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");

            add(bidsTitle, bids);
        }
    }

    private void configureLayout() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.CENTER);

        container.getStyle().setAlignItems(Style.AlignItems.CENTER);
    }

}

