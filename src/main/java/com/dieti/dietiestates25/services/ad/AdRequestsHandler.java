package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.ad.AdView;
import com.google.gson.Gson;
import com.googlecode.gentyref.TypeToken;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
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

    public List<City> getCities(String province) {
        var response = adRequestsService.getCities(province);
        return response.getEntities();
    }

    public void insertAd(AdInsert ad, List<Photo> photos) {
        var adResponse = adRequestsService.insertAd(ad);

        if (adResponse == null)
            return;
        else if (!adResponse.ok()) {
            logger.warn("Failed to create ad for user {}", UserSession.getEmail());
            NotificationFactory.error(adResponse.parse(Ad.class).getMessage());
            return;
        }

        var adId = adResponse.getFirstEntity().getId();
        var failedPhotos = new ArrayList<Photo>();

        if (!photos.isEmpty()) {
            for (var photo : photos) {
                var response = adRequestsService.uploadImages(adId, photo);
                if (response == null || !response.ok())
                    failedPhotos.add(photo);
            }

            if (!failedPhotos.isEmpty()) {
                NotificationFactory.error("There was an error uploading " + failedPhotos.size() + " image(s).");
                logger.warn("Photo upload failed for {} images on ad {}", failedPhotos.size(), adId);
            }
        }

        if (failedPhotos.isEmpty()) {
            logger.info("New ad created by {}", UserSession.getEmail());
            NotificationFactory.success("Ad created successfully!");
        }

        UI.getCurrent().navigate(AdView.class, new RouteParameters("id", String.valueOf(adId))); // even if there may be partial pictures?
    }

    public Ad getAd(int idAd) {
        var response = adRequestsService.getAd(idAd);
        if (response == null)
            return null;

        var ad = response.getFirstEntity();
        var photos = adRequestsService.getImages(idAd);

        if (photos != null) {
            if (photos.ok())
                ad.setPhotos(photos.getEntities());
            else {
                NotificationFactory.error(photos.getMessage());
                logger.warn("Failed to get photos for ad {}.", idAd);
            }
        }

        return ad;
    }

    public Bid sendBid(int adId, double amount, String bidMessage) {
        var response = adRequestsService.sendBid(
                new Bid.Insert(adId, amount, bidMessage, "")
        );

        if (response == null) {
            NotificationFactory.error("Either you already placed a bid or another one was accepted.");
            return null;
        }

        if (response.ok())
            NotificationFactory.success("Bid sent!");

        return response.getFirstEntity();
    }

    public List<Bid> getBidsBy(String key, Serializable value) {
        var response = adRequestsService.getBidsBy(key, value);

        if (response == null) return null;

        if (!response.ok())
            NotificationFactory.error("We couldn't fetch bids for this ad.");

        return response.getEntities();
    }

    public void cancelBid(int bidId) {
        var response = adRequestsService.cancelBid(bidId);

        if (response == null) return;

        if (response.ok())
            NotificationFactory.success("Bid deleted successfully!");
        else
            NotificationFactory.error("We couldn't cancel your bid.");
    }

    public boolean acceptOrRefuseBid(Bid bid) {
        var response = adRequestsService.acceptOrRefuseBid(bid);

        if (response == null) return false;

        if (response.ok()) {
            NotificationFactory.primary("Please refresh this page to see the counteroffer.");
        }
        else
            NotificationFactory.error(response.getRawBody());

        return response.ok();
    }

    public boolean acceptOrRefuseCounterOffer(Bid counteroffer) {
        var response = adRequestsService.acceptOrRefuseCounterOffer(counteroffer);

        if (response == null) return false;

        if (response.ok()) {
            if (counteroffer instanceof Bid.Counteroffer.Refuse)
                NotificationFactory.success("Counter offer refused.");
            else if (counteroffer instanceof Bid.Accept)
                NotificationFactory.success("Counter offer accepted.");
        }
        else
            NotificationFactory.error(response.getRawBody());

        return response.ok();
    }
}
