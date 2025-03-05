package com.dieti.dietiestates25.ui_components;

import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

public class Map extends MapContainer {
    private final LMap map;

    public Map (final LComponentManagementRegistry registry, double initialLat, double initialLng, int initialZoom) {

        super(registry);
        map = super.getlMap();
        map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(registry));
        map.setView(new LLatLng(registry, initialLat, initialLng), initialZoom);

        registry.execJs(
                map.clientComponentJsAccessor() + ".scrollWheelZoom.disable()"
        );
    }

    public static Map createDefaultMap(LComponentManagementRegistry registry) {
        return new Map(registry, 49.6751, 12.1607, 17);
    }

    public LMap getUnderlyingMap() {
        return map;
    }

    public void addMarker(LDefaultComponentManagementRegistry registry, double latitude, double longitude, String label) {
        new LMarker(registry, new LLatLng(registry, latitude, longitude))
                .bindPopup(label)
                .addTo(getUnderlyingMap());
    }
}
