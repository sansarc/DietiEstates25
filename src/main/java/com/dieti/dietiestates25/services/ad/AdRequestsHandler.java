package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.ad.AdView;
import com.dieti.dietiestates25.views.login.LoginView;
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
    public static final String FAILED = "Failed to retrieve ad.";
    AdRequestsService adRequestsService;
    private static final Logger logger = LoggerFactory.getLogger(AdRequestsHandler.class);

    public AdRequestsHandler() { adRequestsService = new AdRequestsService(); }

    public List<String> getRegions() {
        var response = adRequestsService.getRegions();
        if (response == null) return List.of("No regions found.");
        return new Gson().fromJson(response.getRawBody(), new TypeToken<List<String>>() {}.getType());
    }

    public List<String> getProvinces(String region) {
        var response = adRequestsService.getProvinces(region);
        if (response == null) return List.of("No provinces found.");
        List<String> list = new Gson().fromJson(response.getRawBody(), new TypeToken<List<String>>() {}.getType());
        return list.isEmpty()
                ? List.of("No provinces found.")
                : list;
    }

    public List<City> getCities(String province) {
        var response = adRequestsService.getCities(province);
        if (response == null  || response.getEntities().isEmpty()) return List.of(new City("No cities found.", "0"));
        return response.getEntities();
    }

    public void deleteAd(int id) {
        var response = adRequestsService.deleteAd(id);

        System.out.println(UserSession.getCurrentPath());

        if (response == null) return;

        if (response.ok())
            NotificationFactory.success("Ad deleted successfully!" + (UserSession.getCurrentPath().contains("profile") ? " Refresh this page to see the changes." : ""));
        else
            NotificationFactory.error("We couldn't cancel this ad.");
    }

    public void insertAd(AdInsert ad, List<Photo> photos) {
        var adResponse = adRequestsService.insertAd(ad);

        if (adResponse == null)
            return;
        else if (!adResponse.ok()) {
            logger.warn("Failed to create ad.");
            NotificationFactory.error(adResponse.getMessage());
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

        logger.info("New ad created. Ad ID: {}", adId);
        NotificationFactory.success("Ad created successfully!");
        UI.getCurrent().navigate(AdView.class, new RouteParameters("id", String.valueOf(adId)));
    }

    public Ad getAd(int idAd) {
        var search = new Ad.SearchBy();
        search.setId(idAd);
        var response = adRequestsService.searchAds(search);
        if (response == null) return null;

        if (!response.ok()) {
            NotificationFactory.error(FAILED);
            logger.warn("Failed to retrieve ad {}.", idAd);
            return null;
        }

        var ad = response.getFirstEntity();
        retrievePhotos(ad);

        return ad;
    }


    public void retrievePhotos(Ad ad) {
        var photos = adRequestsService.getImages(ad.getId());

        if (photos != null) {
            if (photos.ok())
                ad.setPhotos(photos.getEntities());
            else {
                NotificationFactory.error(photos.getMessage());
                logger.warn("Failed to get photos for ad {}.", ad.getId());
            }
        }
    }

    public Bid sendBid(int adId, double amount, String bidMessage) {
        var response = adRequestsService.sendBid(
                new Bid.Insert(adId, amount, bidMessage, "")
        );

        if (response == null || !response.ok()) {
            if (!UserSession.isUserLoggedIn()) {
                UI.getCurrent().navigate(LoginView.class);
                NotificationFactory.primary("You need to log in first.");
            }
            else
                NotificationFactory.error("Either you already placed a bid or another one was accepted.");

            return null;
        }

        NotificationFactory.success("Bid sent!");
        logger.info("New bid ({}) for ad {}.", response.getFirstEntity().getId(), adId);
        return response.getFirstEntity();
    }

    public List<Bid> getBidsBy(String key, Serializable value) {
        var response = adRequestsService.getBidsBy(key, value);

        if (response == null) return List.of();

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
            NotificationFactory.primary("Refresh this page to see the changes.");
        }
        else
            NotificationFactory.error(response.getRawBody());

        return response.ok();
    }

    public boolean acceptOrRefuseCounterOffer(Bid counteroffer) {
        var response = adRequestsService.acceptOrRefuseCounterOffer(counteroffer);

        if (response == null) return false;

        if (response.ok()) {
            if (counteroffer instanceof Bid.Counteroffer.Refuse) {
                NotificationFactory.success("Counter offer refused.");
                logger.info("Counter offer {} refused for ad {}.", counteroffer.getId(), counteroffer.getAdId());
            }
            else if (counteroffer instanceof Bid.Accept) {
                NotificationFactory.success("Counter offer accepted.");
                logger.info("Counter offer {} accepted for ad {}.", counteroffer.getId(), counteroffer.getAdId());
            }
        }
        else {
            NotificationFactory.error(response.getRawBody());
            logger.warn("Failed to accept or refuse counter offer {} for ad {}.", counteroffer.getId(), counteroffer.getAdId());
        }

        return response.ok();
    }

    public List<Ad> getAdsByAgent(String agentEmail) {
        var search = new Ad.SearchBy();
        search.setAgent(agentEmail);
        var response = adRequestsService.searchAds(search);

        if (badAdResponse(response))
            return List.of();

        return response.getEntities();
    }

    public List<Ad> searchAds(Ad.SearchBy search) {
        var response = adRequestsService.searchAds(search);
        if (badAdResponse(response))
            return List.of();

        var ads = response.getEntities();
        for (var ad : ads) retrievePhotos(ad);

        return ads;
    }

    public boolean badAdResponse(SimpleResponse response) {
        if (response == null) return true;

        if (!response.ok()) {
            NotificationFactory.error(FAILED);
            logger.warn(FAILED);
            return true;
        }
        
        return false;
    } 

}
