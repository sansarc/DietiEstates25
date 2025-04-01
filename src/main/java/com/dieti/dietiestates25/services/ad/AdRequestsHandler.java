package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Bid;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.google.gson.Gson;
import com.googlecode.gentyref.TypeToken;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AdRequestsHandler {
    private final AdRequestsService adRequestsService;
    private final static Logger logger = LoggerFactory.getLogger(AdRequestsHandler.class);

    public AdRequestsHandler() { adRequestsService = new AdRequestsService(); }

    public List<String> getRegions() {
        var response = adRequestsService.getRegions();
        return new Gson().fromJson(response.getRawBody(), new TypeToken<List<String>>() {}.getType());
    }

    public List<String> getProvinces(String region) {
        var response = adRequestsService.getProvinces(region);
        return new Gson().fromJson(response.getRawBody(), new TypeToken<List<String>>() {}.getType());
    }

    public List<String> getCities(String province) {
        var response = adRequestsService.getCities(province);
        return new Gson().fromJson(response.getRawBody(), new TypeToken<List<String>>() {}.getType());
    }

    public void insertAd(Ad ad) {
        SimpleResponse response = adRequestsService.insertAd(ad);

        if (response.getStatusCode() == Constants.Codes.CREATED) {
            logger.info("New ad created by {}", VaadinSession.getCurrent().getAttribute("email"));
            NotificationFactory.success(response.getRawBody());
        } else {
            logger.warn("Failed to create ad for user {}", VaadinSession.getCurrent().getAttribute("email"));
            NotificationFactory.error(response.getRawBody());
        }
    }

    public void sendBid(int adId, double amount, String bidMessage) {
        SimpleResponse response = adRequestsService.sendBid(
                new Bid(adId, amount, bidMessage)
        );

        if (response.getStatusCode() == Constants.Codes.CREATED) {
            logger.info("New bid added by {}", VaadinSession.getCurrent().getAttribute("email"));
            NotificationFactory.success(response.getRawBody());
        } else {
            logger.warn("Failed to send bid for user {}", VaadinSession.getCurrent().getAttribute("email"));
            // NotificationFactory.error(response);
        }
    }
}
