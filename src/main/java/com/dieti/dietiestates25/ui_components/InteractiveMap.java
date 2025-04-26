package com.dieti.dietiestates25.ui_components;

import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class InteractiveMap extends MapContainer {
    private final LMap map;

    public InteractiveMap(final LComponentManagementRegistry registry, String coordinates) {

        super(registry);
        map = super.getlMap();
        map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(registry));

        var lat = Double.parseDouble(coordinates.split(";")[0].trim());
        var lng = Double.parseDouble(coordinates.split(";")[1].trim());
        map.setView(new LLatLng(registry, lat, lng), 17);

        addMarker(registry, lat, lng, "House");
    }

    public static InteractiveMap createDefaultMap(LComponentManagementRegistry registry) {
        return new InteractiveMap(registry, "49.6751 ; 12.1607");
    }

    public void addMarker(LComponentManagementRegistry registry, double latitude, double longitude, String label) {
        new LMarker(registry, new LLatLng(registry, latitude, longitude))
                .bindPopup(label)
                .addTo(map);
    }

}
