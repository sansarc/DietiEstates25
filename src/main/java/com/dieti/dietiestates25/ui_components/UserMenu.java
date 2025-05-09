package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.services.authentication.AuthenticationHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.agency_dashboard.AgencyDashboardView;
import com.dieti.dietiestates25.views.profile.ProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.dom.Style;

public class UserMenu extends Avatar {

    public UserMenu() {
        super(UserSession.getFirstName() + " " + UserSession.getLastName());

        addThemeVariants(AvatarVariant.LUMO_LARGE);
        getStyle()
                .setColor(Constants.Colors.PRIMARY_BLUE)
                .setFontWeight(Style.FontWeight.BOLDER)
                .setBorder("1px solid " + Constants.Colors.PRIMARY_BLUE)
                .setCursor("pointer")
                .setTransform("translateX(-15%)");

        createMenu(); // to show on click
    }

    private void createMenu() {
        var contextMenu = new ContextMenu(this);
        contextMenu.getStyle().setWidth("250px");
        contextMenu.setOpenOnClick(true);

        if (UserSession.isManagerOrAgent())
            contextMenu.addItem("Your Agency", e -> UI.getCurrent().navigate(AgencyDashboardView.class));

        contextMenu.addItem("Profile", e -> UI.getCurrent().navigate(ProfileView.class));
        contextMenu.add(new Hr());

        contextMenu.addItem("Logout", e -> {
            new AuthenticationHandler().logout(UserSession.getSessionId());
            UserSession.logout(false);
        }).getElement().getStyle().setColor("red");
    }
}
