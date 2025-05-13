package com.dieti.dietiestates25.views.home;

import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.AdCard;
import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.registerAgency.ConfirmAccountDialog;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.HashMap;
import java.util.Map;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Dieti Estates | Home")
public class HomeView extends VerticalLayout {

    public static final String WHITE = "white";
    transient AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    Div titleContainer;
    TextField searchText;
    Button searchButton;
    HorizontalLayout searchLayout;
    Div background;
    VerticalLayout backgroundContent;
    VerticalLayout adsList;

    private static final Map<Integer, Ad> TEMP_AD_CACHE = new HashMap<>();

    public HomeView() {
        configureLayout();
        configureComponents();
        if (UserSession.isManagerOrAgent() && !UserSession.isConfirmed())
            new ConfirmAccountDialog().open();
    }

    private void configureComponents() {
        createTitle();
        createSearchLayout();
        createBackground();
        createSecondTitle();

        background.add(backgroundContent);
        add(background, new Hr(), adsList);
    }

    private Ad getOrFetchAd(int id) {
        if (TEMP_AD_CACHE.containsKey(id))
            return TEMP_AD_CACHE.get(id);

        var ad = adRequestsHandler.getAd(id);
        if (ad != null)
            TEMP_AD_CACHE.put(id, ad);

        return ad;
    }

    private void createSecondTitle() {
        var secondTitle = new H1("Latest Listings");
        var subtitle = new Span("Check out the latest uploaded properties in the last days.");

        adsList = new VerticalLayout(secondTitle, subtitle);
        adsList.setSizeFull();
        adsList.setAlignItems(Alignment.CENTER);
        adsList.setAlignSelf(Alignment.START, secondTitle, subtitle);

        var lastAd = getOrFetchAd(0);   // retrieving the latest ad
        if (lastAd != null) {
            var lastId = lastAd.getId();
            for (int i = 0; i < 2; i++) {
                var ad = getOrFetchAd(lastId--);  // then going down
                if (ad != null)
                    adsList.add(new AdCard(ad));
            }
        }

    }

    private void createTitle() {
        var base = new H1("Where");
        var slidingDownPart = new H1("Agents");
        var slidingInPart = new H1("meet Clients.");

        base.getStyle()
                .setColor(WHITE)
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px");
        slidingDownPart.getStyle()
                .setColor(WHITE)
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px")
                .setPosition(Style.Position.RELATIVE)
                .set("animation", "slideDown 2s ease-out");
        slidingInPart.getStyle()
                .setColor(WHITE)
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px")
                .setPosition(Style.Position.RELATIVE)
                .set("animation", "slideIn 3.5s ease-out");

        getElement().appendChild(getAnimations());
        titleContainer = new Div(base, slidingDownPart, slidingInPart);
    }

    private Element getAnimations() {
        Element style = new Element("style");
        style.setText("""
                @keyframes slideIn {
                    0% {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    100% {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
                @keyframes slideDown {
                    0% {
                        transform: translateY(-100%);
                        opacity: 0;
                    }
                    100% {
                        transform: translateY(0);
                        opacity: 1;
                    }
                }
        """);
        return style;
    }

    private void createBackground() {
        background = new Div();
        background.setSizeFull();
        background.setHeight("500px");
        background.getStyle()
                .setPosition(Style.Position.RELATIVE)
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .set("background-image", "url(images/background.jpg)")
                .set("background-size", "cover")
                .set("background-repeat", "no-repeat")
                .set("background-position", "center");

        wrapBackgroundContent();
    }

    private void wrapBackgroundContent() {
        backgroundContent = new VerticalLayout(titleContainer, searchLayout);
        backgroundContent.setSizeFull();
        backgroundContent.getStyle()
                .setTransform("translateY(60px)")
                .setMarginLeft("30px");

    }

    private void createSearchLayout() {
        createSearchText();
        createSearchButton();

        searchLayout = new HorizontalLayout(searchText, searchButton);
        searchLayout.setSpacing(false);
        searchLayout.setWidthFull();
        searchLayout.setPadding(false);
        searchLayout.getStyle()
                .setMarginLeft("10px")
                .setTransform("translateY(25px)");

        searchLayout.setAlignSelf(Alignment.CENTER, searchButton);
    }

    private void createSearchButton() {
        searchButton = MainLayout.getSearchButton(searchText);
        searchButton.setHeight("50px");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickShortcut(Key.ENTER);
    }

    private void createSearchText() {
        searchText = new TextField("", "Where's your next home?");
        searchText.setAutocomplete(Autocomplete.OFF);
        searchText.setWidth("35%");
        searchText.getStyle()
                .setBackgroundColor("transparent")
                .set("--vaadin-input-field-placeholder-color", WHITE)
                .set("--vaadin-input-field-height", "50px")
                .set("--vaadin-input-field-value-color", WHITE)
                .set("--vaadin-input-field-value-font-size", "24px")
                .set("--vaadin-input-field-value-font-weight", LumoUtility.FontWeight.BOLD);
    }

    private void configureLayout() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setPadding(false);
    }

}
