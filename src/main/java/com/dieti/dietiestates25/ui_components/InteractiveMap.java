package com.dieti.dietiestates25.ui_components;

import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.ui.LPopup;
import software.xdev.vaadin.maps.leaflet.layer.ui.LPopupOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapZoomPanOptions;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;

public class InteractiveMap extends MapContainer {
    private final LMap map;
    private final List<LMarker> markers = new ArrayList<>();

    public InteractiveMap(final LComponentManagementRegistry registry, String coordinates, int zoomOnMap) {
        super(registry);
        map = super.getlMap();
        map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(registry));

        if (zoomOnMap <= 0) zoomOnMap = 17;
        map.setView(parseCoordinates(registry, coordinates), zoomOnMap);
    }

    public static InteractiveMap createDefaultMap(LComponentManagementRegistry registry) {
        return new InteractiveMap(registry, "49.6751 ; 12.1607", 4);
    }

    public void addMarker(LComponentManagementRegistry registry, String coordinates, String label) {
        var options = new LPopupOptions();
        options.setCloseButton(false);
        options.setCloseOnClick(false);
        options.setCloseOnEscapeKey(false);

        var position = parseCoordinates(registry, coordinates);

        var popup = new LPopup(registry, options).setContent(label);
        var marker = new LMarker(registry, position).addTo(map);
        marker.bindPopup(popup);
        marker.openPopup();

        markers.add(marker);  // tracking the markers so I can remove them later
    }

    public void moveToLocationSmoothly(LComponentManagementRegistry registry, String coordinates) {
        var options = new LMapZoomPanOptions();
        options.setDuration(1.5);
        options.setAnimate(true);

        var position = parseCoordinates(registry, coordinates);
        map.setView(position, 17, options);
    }


    public void removeAllMarkers() {
        for (LMarker marker : markers)
            map.removeLayer(marker);

        markers.clear();
    }

    private LLatLng parseCoordinates(LComponentManagementRegistry registry, String coordinates) {
        var parts = coordinates.split(";");
        double lat = Double.parseDouble(parts[0].trim());
        double lng = Double.parseDouble(parts[1].trim());
        return new LLatLng(registry, lat, lng);
    }
}