package com.dieti.dietiestates25.views.upload;

import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.upload.forms.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "upload", layout = MainLayout.class)
@PageTitle("Upload")
public class UploadView extends VerticalLayout {

    private Tabs tabs;
    private Div tabsContent;
    private Tab generalInfoTab;
    private Tab detailsTab;
    private Tab costsInfoTab;
    private Tab descriptionNMediaTab;
    private Div generalInfoContent;
    private Div detailsContent;
    private Div costsInfoContent;
    private Div descriptionNMediaContent;

    public UploadView() {
        configureLayout();
        configureComponents();
    }

    private void configureComponents() {
        generalInfoTab = new Tab("General Information");
        detailsTab = new Tab("Details");
        costsInfoTab = new Tab("Costs Information");
        descriptionNMediaTab = new Tab("Description & Media");

        generalInfoContent = createTabContent(new GeneralInfoForm());
        detailsContent = createTabContent(new DetailsForm());
        costsInfoContent = createTabContent(new CostsForm());
        descriptionNMediaContent = createTabContent(new DescriptionNMediaForm());

        tabs = createTabs();
        tabsContent = createTabsContent();

        setupTabVisibility();

        var stepsLayout = createStepsLayout();
        add(new H2("Upload"), stepsLayout);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

    private Tabs createTabs() {
        var tabs = new Tabs(generalInfoTab, detailsTab, costsInfoTab, descriptionNMediaTab);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.setWidthFull();
        return tabs;
    }

    private Div createTabsContent() {
        var content = new Div(generalInfoContent, detailsContent, costsInfoContent, descriptionNMediaContent);
        content.setSizeFull();
        return content;
    }

    private void setupTabVisibility() {
        tabs.addSelectedChangeListener(event -> {
            generalInfoContent.setVisible(false);
            detailsContent.setVisible(false);
            costsInfoContent.setVisible(false);
            descriptionNMediaContent.setVisible(false);

            if (event.getSelectedTab().equals(generalInfoTab)) {
                generalInfoContent.setVisible(true);
            } else if (event.getSelectedTab().equals(detailsTab)) {
                detailsContent.setVisible(true);
            } else if (event.getSelectedTab().equals(costsInfoTab)) {
                costsInfoContent.setVisible(true);
            } else if (event.getSelectedTab().equals(descriptionNMediaTab)) {
                descriptionNMediaContent.setVisible(true);
            }
        });

        generalInfoContent.setVisible(true);
        detailsContent.setVisible(false);
        costsInfoContent.setVisible(false);
        descriptionNMediaContent.setVisible(false);
    }

    private VerticalLayout createStepsLayout() {
        var layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(tabs, tabsContent);
        return layout;
    }

    private Div createTabContent(Component content) {
        return new Div(wrapInVerticalLayout(content));
    }

    private VerticalLayout wrapInVerticalLayout(Component content) {
        var continueButton = new Button("Continue");
        continueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        continueButton.setWidth("15%");
        var backButton = new Button("Back");
        backButton.setWidth("15%");

        var buttonLayout = new HorizontalLayout(backButton, continueButton);
        buttonLayout.setWidth("80%");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);


        continueButton.addClickListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab.equals(generalInfoTab) /* && generalInfoForm.areRequiredFieldsValid() */ ) {
                generalInfoTab.getStyle().setColor("green");
                tabs.setSelectedTab(detailsTab);
            } else if (selectedTab.equals(detailsTab)) {
                detailsTab.getStyle().setColor("green");
                tabs.setSelectedTab(costsInfoTab);
            } else if (selectedTab.equals(costsInfoTab)) {
                costsInfoTab.getStyle().setColor("green");
                tabs.setSelectedTab(descriptionNMediaTab);
            } else {
                Notification.show("Fill all the required fields to continue.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        backButton.addClickListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            selectedTab.getStyle().remove("color");
            if(selectedTab.equals(detailsTab)) {
                tabs.setSelectedTab(generalInfoTab);
            } else if(selectedTab.equals(costsInfoTab)) {
                tabs.setSelectedTab(detailsTab);
            } else if(selectedTab.equals(descriptionNMediaTab)) {
                tabs.setSelectedTab(costsInfoTab);
            } else {
                Notification.show("You're at the beginning of the form")
                        .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            }
        });

        var contentContainer = new HorizontalLayout(content);
        contentContainer.setSizeFull();
        contentContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignSelf(Alignment.CENTER, contentContainer);

        var layout = new VerticalLayout(contentContainer, buttonLayout);
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }
}