package com.dieti.dietiestates25.views;

import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        var title = new H2("Dieti Estates");
        var navigation = getNavigation();

        // Add margin to create space between title and navigation
        navigation.getStyle().set("margin-left", "2em");

        header.add(title, navigation);
        addToNavbar(header);
    }

    private HorizontalLayout getNavigation() {
        HorizontalLayout navigation = new HorizontalLayout();
        navigation.setSpacing(true);
        navigation.setPadding(true);
        navigation.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink dashboardLink = new RouterLink("Dashboard", LoginView.class);
        RouterLink ordersLink = new RouterLink("Orders", LoginView.class);
        RouterLink customersLink = new RouterLink("Customers", LoginView.class);
        RouterLink productsLink = new RouterLink("Products", LoginView.class);

        // Add consistent spacing between navigation items
        navigation.setSpacing(true);
        navigation.getStyle().set("gap", "1.5em");

        navigation.add(
                dashboardLink,
                ordersLink,
                customersLink,
                productsLink
        );

        return navigation;
    }
}
