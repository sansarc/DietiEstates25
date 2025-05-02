package com.dieti.dietiestates25.views.search;

import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.*;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import org.vaadin.lineawesome.LineAwesomeIcon;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@PageTitle("Search")
@Route(value = "search", layout = MainLayout.class)
public class SearchView extends VerticalLayout implements HasUrlParameter<String> {

    AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    private VerticalLayout adsList;
    private InteractiveMap defaultMap, map;
    private Select<String> sortByPrice, agencyFilter;
    private List<Ad> sortedAds;
    private HorizontalLayout adsFilters;
    private Div mapDiv;
    private LDefaultComponentManagementRegistry registry;
    private boolean isMapDefault;

    private Form form;
    private Select<String> type;
    private NumberField minPrice, maxPrice;
    private IntegerField nRooms, nBathrooms;
    private Form.LocationForm locationComponents;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String ignored) {
        var queryParams = event.getLocation().getQueryParameters().getParameters();

        var type = getFirstParam(queryParams, "q"); // "sale" or "rent"
        var nRooms = pareInt(getFirstParam(queryParams, "nrooms"));
        var nBathrooms = pareInt(getFirstParam(queryParams, "nbathrooms"));
        var region = getFirstParam(queryParams, "region");
        var province = getFirstParam(queryParams, "province");
        var city = getFirstParam(queryParams, "city");
        var address = getFirstParam(queryParams, "address");
        var minPrice = parseDouble(getFirstParam(queryParams, "min"));
        var maxPrice = parseDouble(getFirstParam(queryParams, "max"));

        if (type != null || nRooms > 0 || nBathrooms > 0 || region != null || province != null || city != null || address != null || maxPrice > 0 || minPrice > 0) {
            var search = new Ad.SearchBy(
                    type != null && type.equalsIgnoreCase("rent") ? "R" : "S",
                    nRooms, nBathrooms,
                    region, province, city, address,
                    minPrice, maxPrice
            );
            var ads = adRequestsHandler.searchAds(search);
            form.clear();
            fillForm(search);
            refresh(ads);
        }
    }

    private void fillForm(Ad.SearchBy search) {
        type.setValue(search.getType().equals("Rent") ? "For Rent" : "For Sale");
        nRooms.setValue(search.getNRooms());
        nBathrooms.setValue(search.getNBathrooms());
        locationComponents.region.setValue(search.getRegion());
        locationComponents.province.setValue(search.getProvince());
        locationComponents.city.setValue(search.getCity());
        minPrice.setValue(search.getMinPrice());
        maxPrice.setValue(search.getMaxPrice());
    }

    private String getFirstParam(Map<String, List<String>> params, String key) {
        var values = params.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    private int pareInt(String value) {
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public SearchView() {
        configureComponents();
        configureLayout();
    }

    private void configureComponents() {
        type = Form.select("Type", "For Sale", "For Rent");
        type.setValue("For Sale");
        nRooms = Form.integerField("Rooms", LineAwesomeIcon.COUCH_SOLID.create());
        nBathrooms = Form.integerField("Bathrooms", LineAwesomeIcon.BATH_SOLID.create());
        minPrice = Form.priceInEuroNumberField("Min Price");
        maxPrice = Form.priceInEuroNumberField("Max Price");
        locationComponents = new Form.LocationForm();

        form = new Form(type, minPrice, maxPrice, nRooms, nBathrooms);
        form.add(locationComponents.asArray());
        form.setMaxWidth("1200px");
        form.getStyle().setMargin("0 auto").setPadding("var(--lumo-space-xxs)").setMarginBottom("5px");
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 8)
        );
        form.getChildren()          // get all form's component bigger except min and max price (NumberField)
                .forEach(component -> {
                    if (!(component instanceof NumberField))
                        form.setColspan(component, 2);
                });

        var search = new Button("Search", event -> {
            var isValid = new AtomicBoolean(false);
            form.getChildren()
                    .map(HasValue.class::cast)
                    .forEach(component -> {
                        if ((component.getValue() != null))
                            isValid.set(true);
                    });

            if (isValid.get()) {
                setEnabled(false);
                var query = buildQueryParams(type.getValue(), nRooms.getValue(), nBathrooms.getValue(), locationComponents, minPrice.getValue(), maxPrice.getValue());
                UI.getCurrent().getPage().getHistory().replaceState(null, "search" + query);

                var ads = adRequestsHandler.searchAds(new Ad.SearchBy(
                        type.getValue().substring(4, 5),
                        nRooms.getValue(),
                        nBathrooms.getValue(),
                        locationComponents.getRegion(),
                        locationComponents.getProvince(),
                        locationComponents.getCity(),
                        locationComponents.getAddress(),
                        minPrice.getValue() == null ? 0 : minPrice.getValue(),
                        maxPrice.getValue() == null ? 0 : maxPrice.getValue()

                ));
                refresh(ads);
                setEnabled(true);
            }
            else NotificationFactory.error("Fill at least one field to make a search.");
        });
        search.addClickShortcut(Key.ENTER);
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        search.getStyle().setCursor("pointer");
        search.setWidth("20px");

        var clear = new Button(VaadinIcon.CLOSE.create(), event -> form.clear());
        clear.setTooltipText("Clear form");
        clear.getStyle().setCursor("pointer");

        var formLayout = new VerticalLayout(form, new HorizontalLayout(search, clear), new Hr());
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(false);
        formLayout.setMargin(false);
        formLayout.setPadding(false);

        sortByPrice = Form.select("Sort By Price", "None", "Low to High", "High to Low");
        sortByPrice.setValue("None");
        sortByPrice.getStyle().setMarginTop("-25px");
        sortByPrice.addThemeVariants(SelectVariant.LUMO_SMALL);
        sortByPrice.addValueChangeListener(event -> refresh(sortedAds));

        agencyFilter = Form.select("Filter By Agency", "All"); // default option
        agencyFilter.setValue("All");
        agencyFilter.getStyle().setMarginTop("-25px");
        agencyFilter.addThemeVariants(SelectVariant.LUMO_SMALL);
        agencyFilter.addValueChangeListener(event -> refresh(sortedAds));

        adsFilters = new HorizontalLayout(sortByPrice, agencyFilter);
        adsFilters.setWidthFull();
        adsFilters.setAlignItems(Alignment.CENTER);
        adsFilters.setJustifyContentMode(JustifyContentMode.START);

        adsList = new VerticalLayout(adsFilters, new Span("Results will appear here."));
        adsList.setWidth("40%");
        adsList.getStyle().setOverflow(Style.Overflow.AUTO); // scroll if many ads

        registry = new LDefaultComponentManagementRegistry(this);
        defaultMap = InteractiveMap.createDefaultMap(registry);
        defaultMap.setSizeFull();
        isMapDefault = true;
        mapDiv = new DivContainer("auto", "98%");
        mapDiv.getStyle().setMarginRight("-20px").setPadding("4px");
        mapDiv.add(defaultMap);

        var adsNMap = new SplitLayout();
        adsNMap.setSizeFull();
        adsNMap.addToSecondary(mapDiv);
        adsNMap.addToPrimary(adsList);
        adsNMap.setSplitterPosition(50);
        adsNMap.getStyle().setMarginTop("-20px");

        add(formLayout, adsNMap);
    }

    private void refresh(List<Ad> ads) {
        adsList.getChildren()   // basically remove all except the sort combobox
                .filter(component -> component instanceof Span || component instanceof AdCard)
                .forEach(adsList::remove);
        adsFilters.getChildren()
                .filter(Span.class::isInstance)
                .forEach(adsFilters::remove);

        // reset map
        if (map != null) {
            map.removeAllMarkers();
            mapDiv.remove(map);
            mapDiv.add(defaultMap);
            isMapDefault = true;
        }

        if (ads == null || ads.isEmpty()) {
            adsList.add(new Span("No ads found."));
            sortedAds.clear();
            return;
        }

        adsFilters.add(new Span("Found " + ads.size() + " ads."));
        sortedAds = new ArrayList<>(ads);
        sortedAds.sort((a1, a2) -> {
            if (sortByPrice.getValue().equals("Low to High"))
                return Double.compare(a1.getPrice(), a2.getPrice());
            else if (sortByPrice.getValue().equals("High to Low"))
                return Double.compare(a2.getPrice(), a1.getPrice());

            return 0; // default
        });
        var agencies = sortedAds.stream()
                .map(ad -> ad.getAgent().getAgencyName())
                .filter(name -> name != null && !name.isEmpty())
                .distinct()
                .sorted()
                .toList();
        agencyFilter.setItems("All");
        agencyFilter.setItems(Stream.concat(Stream.of("All"), agencies.stream()).toList());

        for (var ad : sortedAds) {
            if (ad.getCoordinates() != null) {
                var selectedAgency = agencyFilter.getValue();
                if (selectedAgency != null && !"All".equals(selectedAgency) && !selectedAgency.equals(ad.getAgent().getAgencyName()))
                    continue;

                if (isMapDefault) {
                    // map component is not the friendliest of them all
                    map = new InteractiveMap(registry, ad.getCoordinates(), 6);
                    map.setSizeFull();
                    mapDiv.remove(defaultMap);
                    mapDiv.add(map);
                    isMapDefault = false;
                }
                if (map != null)
                    map.addMarker(registry, ad.getCoordinates(), ad.getAddress());
            }

            var card = new AdCard(ad);
            var viewOnMap = new Anchor("#", "View on Map");

            if (ad.getCoordinates() == null) {
                viewOnMap.setEnabled(false);
                new InfoPopover(viewOnMap, "We couldn't retrieve the coordinates of this address (" + ad.getAddress() + ").");
            }
            else
                viewOnMap.getElement().addEventListener("click", event -> map.moveToLocationSmoothly(registry, ad.getCoordinates()))
                        .addEventData("event.preventDefault()");

            card.addToFooter(viewOnMap);
            adsList.add(card);
        }

    }

    private String buildQueryParams(String type, Integer nRooms, Integer nBathrooms, Form.LocationForm loc, Double minPrice, Double maxPrice) {
        var string = new StringBuilder();
        string.append("?q=").append(type.toLowerCase().contains("rent") ? "rent" : "sale");

        if (nRooms != null && nRooms > 0)
            string.append("&nrooms=").append(nRooms);

        if (nBathrooms != null && nBathrooms > 0)
            string.append("&nbathrooms=").append(nBathrooms);

        if (loc.getRegion() != null && !loc.getRegion().isEmpty())
            string.append("&region=").append(loc.getRegion());

        if (loc.getProvince() != null && !loc.getProvince().isEmpty())
            string.append("&province=").append(loc.getProvince());

        if (loc.getCity() != null && !loc.getCity().isEmpty())
            string.append("&city=").append(loc.getCity());

        if (loc.getAddress() != null && !loc.getAddress().isEmpty())
            string.append("&address=").append(loc.getAddress());

        if (minPrice != null && minPrice > 0)
            string.append("&min=").append(minPrice);

        if (maxPrice != null && maxPrice > 0)
            string.append("&max=").append(maxPrice.intValue());

        return string.toString();
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

}
