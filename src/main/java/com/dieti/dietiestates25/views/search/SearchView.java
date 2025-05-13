package com.dieti.dietiestates25.views.search;

import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.*;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.HasValue;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@PageTitle("Search")
@Route(value = "search", layout = MainLayout.class)
public class SearchView extends VerticalLayout implements HasUrlParameter<String> {

    public static final String FOR_SALE = "For Sale";
    public static final String FOR_RENT = "For Rent";
    transient AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    VerticalLayout adsList;
    InteractiveMap map;
    Select<String> sortByPrice;
    transient Select<String> agencyFilter;
    transient ArrayList<Ad> sortedAds;
    HorizontalLayout adsFilters;
    Span adsFoundCount;
    Div mapDiv;
    LDefaultComponentManagementRegistry registry;
    boolean isMapDefault;

    Form form;
    Select<String> type;
    NumberField minPrice;
    NumberField maxPrice;
    IntegerField nRooms;
    IntegerField nBathrooms;
    Form.LocationForm locationComponents;
    Button search;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String ignored) {
        var queryParams = event.getLocation().getQueryParameters().getParameters();
        Ad.SearchBy searchParams;

        var locationAny = getFirstParam(queryParams, "locationAny");
        if (locationAny != null && !locationAny.isEmpty()) {
            searchParams = new Ad.SearchBy(locationAny);
            searchNRefresh(searchParams);
            return;
        }

        var type = getFirstParam(queryParams, "q"); // "sale" or "rent"
        var nRooms = parseInt(getFirstParam(queryParams, "nrooms"));
        var nBathrooms = parseInt(getFirstParam(queryParams, "nbathrooms"));
        var region = getFirstParam(queryParams, "region");
        var province = getFirstParam(queryParams, "province");
        var city = getFirstParam(queryParams, "city");
        var address = getFirstParam(queryParams, "address");
        var minPrice = parseDouble(getFirstParam(queryParams, "min"));
        var maxPrice = parseDouble(getFirstParam(queryParams, "max"));

        if (type != null || nRooms > 0 || nBathrooms > 0 || region != null || province != null || city != null || address != null || maxPrice > 0 || minPrice > 0) {
            searchParams = new Ad.SearchBy(
                    type != null && (type.equalsIgnoreCase("rent") || type.equalsIgnoreCase("sale")) ? type : "",
                    nRooms, nBathrooms,
                    region, province, city, address,
                    minPrice, maxPrice
            );
            fillForm(searchParams);
            searchNRefresh(searchParams);
        }
    }

    private void searchNRefresh(Ad.SearchBy search) {
        var ads = adRequestsHandler.searchAds(search);
        refresh(ads);
    }

    private void fillForm(Ad.SearchBy search) {
        String typeValue;
        if (search.getType().equalsIgnoreCase("rent"))
            typeValue = FOR_RENT;
        else if (search.getType().equalsIgnoreCase("sale"))
            typeValue = FOR_SALE;
        else
            typeValue = "All";

        type.setValue(typeValue);
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
        return (values != null && !values.isEmpty()) ? values.getFirst() : null;
    }

    private int parseInt(String value) {
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
        type = Form.select("Type", "All", FOR_SALE, FOR_RENT);
        type.setValue("All");
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

        search = getSearchButton();

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

        agencyFilter = new Select<>("Filter by agency", event -> {
            if (event.getValue() != null)
                /* re- */ display();
        });
        agencyFilter.setEmptySelectionAllowed(false);
        agencyFilter.getStyle().setMarginTop("-25px");
        agencyFilter.addThemeVariants(SelectVariant.LUMO_SMALL);

        adsFoundCount = new Span("Results will appear below.");
        adsFoundCount.getStyle().setColor("gray");

        adsFilters = new HorizontalLayout(sortByPrice, agencyFilter, adsFoundCount);
        adsFilters.setWidthFull();
        adsFilters.setAlignItems(Alignment.CENTER);
        adsFilters.setJustifyContentMode(JustifyContentMode.START);

        adsList = new VerticalLayout(adsFilters);
        adsList.setWidth("40%");
        adsList.getStyle().setOverflow(Style.Overflow.AUTO); // scroll if many ads


        registry = new LDefaultComponentManagementRegistry(this);
        mapDiv = new DivContainer("auto", "98%");
        mapDiv.getStyle().setMarginRight("-20px").setPadding("4px");
        mapDiv.add(InteractiveMap.createDefaultMap(registry));
        isMapDefault = true;

        var adsNMap = new SplitLayout();
        adsNMap.setSizeFull();
        adsNMap.addToSecondary(mapDiv);
        adsNMap.addToPrimary(adsList);
        adsNMap.setSplitterPosition(50);
        adsNMap.getStyle().setMarginTop("-20px");

        add(formLayout, adsNMap);
    }

    private boolean isFormValid() {
        var isValid = new AtomicBoolean(false);
        form.getChildren()
                .map(HasValue.class::cast)
                .forEach(component -> {
                    if (/*(!(component instanceof Select<?>)) &&*/
                            component.getValue() != null &&
                            !component.getValue().toString().isBlank())
                        isValid.set(true);
                });

        return isValid.get();
    }

    private Button getSearchButton() {
        search = new Button("Search", event -> {
            handleSearchClick();
            search.setEnabled(true);
        });

        search.setDisableOnClick(true);
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        search.getStyle().setCursor("pointer");
        search.setWidth("20px");

        return search;
    }

    private void handleSearchClick() {
        if (!isFormValid()) {
            NotificationFactory.error("Fill at least one field to make a search.");
            return;
        }

        var query = buildQueryParams(type.getValue(), nRooms.getValue(), nBathrooms.getValue(),
                locationComponents, minPrice.getValue(), maxPrice.getValue());
        UI.getCurrent().getPage().getHistory().replaceState(null, "search" + query);

        var ads = adRequestsHandler.searchAds(new Ad.SearchBy(
                getTypeValue(),
                nRooms.getValue() == null ? 0 : nRooms.getValue(),
                nBathrooms.getValue() == null ? 0 : nBathrooms.getValue(),
                locationComponents.getRegion(),
                locationComponents.getProvince(),
                locationComponents.getCity(),
                locationComponents.getAddress(),
                minPrice.getValue() == null ? 0 : minPrice.getValue(),
                maxPrice.getValue() == null ? 0 : maxPrice.getValue()
        ));

        refresh(ads);
    }

    private String getTypeValue() {
        if (type.getValue() == null) return "";

        return switch (type.getValue()) {
            case FOR_SALE -> "S";
            case FOR_RENT -> "R";
            default -> "";
        };
    }

    public void refresh(List<Ad> ads) {
        agencyFilter.clear();
        sortByPrice.setValue("None");

        if (ads == null || ads.isEmpty()) {
            adsFoundCount.setText("No ads found.");
            clearPreviousData();
            resetMap();
            return;
        }

        sortAds(ads);
        updateAgencyFilter();
        display();
    }

    private void display() {
        clearPreviousData();
        resetMap();

        // filter by selected agency
        var selectedAgency = agencyFilter.getValue();

        var filteredAds = selectedAgency == null || selectedAgency.equals("All")
                ? sortedAds
                : sortedAds.stream()
                .filter(ad -> ad.getAgent().getAgencyName() != null)
                .filter(ad -> ad.getAgent().getAgencyName().equals(selectedAgency))
                .toList();

        var i = 0;
        for (var ad : filteredAds) { i++;
            if (ad.getCoordinates() != null) {
                if (isMapDefault) {
                    // initializing map at the first valid ad with valid coordinates and setting central view there
                    map = new InteractiveMap(registry, ad.getCoordinates(), 6);
                    map.setSizeFull();
                    mapDiv.removeAll();  // replacing default map with the new updated one
                    mapDiv.add(map);
                    isMapDefault = false;
                }
                if (map != null)
                    map.addMarker(registry, ad.getCoordinates(), ad.getAddress());
            }

            var card = new AdCard(ad);
            var viewOnMap = new Anchor("#", "View on Map | ads n: " + i);

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

        adsFoundCount.setText("Found " + filteredAds.size() + " ads.");
    }

    private void updateAgencyFilter() {
        // update for agencies
        var agencies = sortedAds.stream()
                .map(ad -> ad.getAgent().getAgencyName())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        agencyFilter.setItems(Stream.concat(Stream.of("All"), agencies.stream()).toList());
        agencyFilter.setValue("All");
    }

    private void sortAds(List<Ad> ads) {
        sortedAds = new ArrayList<>(ads);

        // price sorting
        sortedAds.sort((a1, a2) -> {
            if (sortByPrice.getValue().equals("Low to High"))
                return Double.compare(a1.getPrice(), a2.getPrice());
            else if (sortByPrice.getValue().equals("High to Low"))
                return Double.compare(a2.getPrice(), a1.getPrice());

            return 0; // default
        });
    }

    private void resetMap() {
        if (map != null) {
            map.removeAllMarkers();
            mapDiv.removeAll();  // replace previous search's map back with the default one, in case next search provides no coordinates-valid results
            mapDiv.add(InteractiveMap.createDefaultMap(registry));
            isMapDefault = true;
        }
    }

    private void clearPreviousData() {
        adsList.removeAll();
        adsList.add(adsFilters);
    }

    String buildQueryParams(String type, Integer nRooms, Integer nBathrooms, Form.LocationForm loc, Double minPrice, Double maxPrice) {
        StringBuilder params = new StringBuilder();
        params.append("?q=");

        if (type != null) {
            if (FOR_SALE.equals(type))
                params.append("sale");
            else if (FOR_RENT.equals(type))
                params.append("rent");
            else
                params.append("all");
        }

        appendIfPositive(params, "&nrooms=", nRooms);
        appendIfPositive(params, "&nbathrooms=", nBathrooms);

        appendIfNotEmpty(params, "&region=", loc.getRegion());
        appendIfNotEmpty(params, "&province=", loc.getProvince());
        appendIfNotEmpty(params, "&city=", loc.getCity());
        appendIfNotEmpty(params, "&address=", loc.getAddress());

        appendIfPositive(params, "&min=", minPrice);
        appendIfPositive(params, "&max=", maxPrice);

        return params.toString();
    }

    private void appendIfPositive(StringBuilder sb, String prefix, Number value) {
        if (value != null && value.doubleValue() > 0)
            sb.append(prefix).append(value);
    }

    private void appendIfNotEmpty(StringBuilder sb, String prefix, String value) {
        if (value != null && !value.isEmpty())
            sb.append(prefix).append(value);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

}