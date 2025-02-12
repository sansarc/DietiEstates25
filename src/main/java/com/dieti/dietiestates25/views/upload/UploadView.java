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
    private final Tabs tabs;
    private final Div tabsContent;
    private final Tab generalInfoTab;
    private final Tab detailsTab;
    private final Tab costsInfoTab;
    private final Tab descriptionNMediaTab;
    private final Div generalInfoContent;
    private final Div detailsContent;
    private final Div costsInfoContent;
    private final Div descriptionNMediaContent;

    private final UploadForm generalInfoForm = new GeneralInfoForm();

    public UploadView() {
        configureLayout();

        generalInfoTab = new Tab("General Information");
        detailsTab = new Tab("Details");
        costsInfoTab = new Tab("Costs Information");
        descriptionNMediaTab = new Tab("Description & Media");

        generalInfoContent = createTabContent(generalInfoForm);
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
        continueButton.setWidth("25%");
        setAlignSelf(Alignment.CENTER, continueButton);

        continueButton.addClickListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab.equals(generalInfoTab) && generalInfoForm.areRequiredFieldsValid()) {
                tabs.setSelectedTab(detailsTab);
            } else if (selectedTab.equals(detailsTab)) {
                tabs.setSelectedTab(costsInfoTab);
            } else if (selectedTab.equals(costsInfoTab)) {
                tabs.setSelectedTab(descriptionNMediaTab);
            } else {
                Notification.show("Fill all the required fields to continue.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        var contentContainer = new HorizontalLayout(content);
        contentContainer.setSizeFull();
        contentContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignSelf(Alignment.CENTER, contentContainer);

        var layout = new VerticalLayout(contentContainer, continueButton);
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }
}