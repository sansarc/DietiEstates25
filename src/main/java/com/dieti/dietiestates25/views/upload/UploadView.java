package com.dieti.dietiestates25.views.upload;

import com.dieti.dietiestates25.annotations.roles_only.ManagerOrAgentOnly;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.upload.forms.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@ManagerOrAgentOnly
@Route(value = "upload", layout = MainLayout.class)
@PageTitle("Upload")
public class UploadView extends VerticalLayout implements BeforeLeaveObserver {

    public static final String GREEN = "green";

    Tabs tabs;
    Div tabsContent;
    Tab generalInfoTab;
    Tab detailsTab;
    Tab descriptionNMediaTab;
    VerticalLayout generalInfoContent;
    VerticalLayout detailsContent;
    VerticalLayout descriptionNMediaContent;

    GeneralInfoForm generalInfoForm;
    DetailsForm detailsForm;
    DescriptionNMediaForm descriptionNMediaForm;

    transient AdRequestsHandler adRequestsHandler = new AdRequestsHandler();
    transient AdInsert ad = new AdInsert();
    transient List<Photo> photos = new ArrayList<>();

    public UploadView() {
        configureLayout();
        configureComponents();
    }

    private void configureComponents() {
        generalInfoTab = new Tab("General Information");
        detailsTab = new Tab("Details");
        descriptionNMediaTab = new Tab("Description & Media");

        generalInfoForm = new GeneralInfoForm();
        detailsForm = new DetailsForm();
        descriptionNMediaForm = new DescriptionNMediaForm();

        generalInfoContent = createTabContent(generalInfoForm);
        detailsContent = createTabContent(detailsForm);
        descriptionNMediaContent = createTabContent(descriptionNMediaForm);

        tabs = createTabs();
        tabsContent = createTabsContent();

        setupTabVisibility();

        var stepsLayout = createStepsLayout();
        add(new H2("Upload a new house"), stepsLayout);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

    private Tabs createTabs() {
        var tabsTmp = new Tabs(generalInfoTab, detailsTab, descriptionNMediaTab);
        tabsTmp.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        tabsTmp.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabsTmp.setWidthFull();
        tabsTmp.getElement().getStyle().set("pointer-events", "none");
        return tabsTmp;
    }

    private Div createTabsContent() {
        var content = new Div(generalInfoContent, detailsContent, descriptionNMediaContent);
        content.setSizeFull();
        return content;
    }

    private void setupTabVisibility() {
        tabs.addSelectedChangeListener(event -> {
            generalInfoContent.setVisible(false);
            detailsContent.setVisible(false);
            descriptionNMediaContent.setVisible(false);

            if (event.getSelectedTab().equals(generalInfoTab)) {
                generalInfoContent.setVisible(true);
            } else if (event.getSelectedTab().equals(detailsTab)) {
                detailsContent.setVisible(true);
            } else if (event.getSelectedTab().equals(descriptionNMediaTab)) {
                descriptionNMediaContent.setVisible(true);
            }
        });

        generalInfoContent.setVisible(true);
        detailsContent.setVisible(false);
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

    private VerticalLayout createTabContent(Component content) {
        return wrapInVerticalLayout(content);
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
        if (content instanceof DescriptionNMediaForm)
            buttonLayout.getStyle().setTransform("translateY(-230%)");

        continueButton.addClickListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab.equals(generalInfoTab) && generalInfoForm.areRequiredFieldsValid() ) {
                generalInfoTab.getStyle().setColor(GREEN);
                tabs.setSelectedTab(detailsTab);
                generalInfoForm.addFormValues(ad);
            } else if (selectedTab.equals(detailsTab) && detailsForm.areRequiredFieldsValid()) {
                detailsTab.getStyle().setColor(GREEN);
                tabs.setSelectedTab(descriptionNMediaTab);
                detailsForm.addFormValues(ad);
            } else if (selectedTab.equals(descriptionNMediaTab) && descriptionNMediaForm.areRequiredFieldsValid()) {
                descriptionNMediaTab.getStyle().setColor(GREEN);
                descriptionNMediaForm.addFormValuesNPhotos(ad, photos);

                var dialog = new ConfirmDialog();
                dialog.setHeader("Confirm");
                dialog.setText("Are you sure you want to continue?");
                dialog.setCancelable(true);
                dialog.setConfirmText("Next");
                dialog.setCancelText("Cancel");

                dialog.addConfirmListener(confirm -> adRequestsHandler.insertAd(ad, photos));

                dialog.open();
            }

        });

        backButton.addClickListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            selectedTab.getStyle().remove("color");
            if (selectedTab.equals(descriptionNMediaTab)) {
                tabs.setSelectedTab(detailsTab);
            } else if (selectedTab.equals(detailsTab)) {
                tabs.setSelectedTab(generalInfoTab);
            } else {
                var notification = Notification.show("You're at the beginning of the form");
                notification.setId("form-start-notification");
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

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        var postponeNavigationAction = event.postpone();
        if (!generalInfoForm.isEmpty())
            new ConfirmDialog("Unsaved changes", "Are you sure?", "Leave", e -> postponeNavigationAction.proceed(), "Stay", e -> {}).open();
    }

}