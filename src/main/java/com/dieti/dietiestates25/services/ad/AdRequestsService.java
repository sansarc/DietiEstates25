package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.constants.Constants.*;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.dto.EntityResponse;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.dieti.dietiestates25.constants.Constants.Codes.INTERNAL_SERVER_ERROR;

public class AdRequestsService {

    public static final String SESSION_ID = "sessionId";
    RequestService requestService;

    public AdRequestsService() {
        requestService = new RequestService();
    }

    public SimpleResponse getRegions() {
        var response = requestService.GET(ApiEndpoints.GET_REGIONS);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse getProvinces(String region) {
        Map<String, Serializable> regionParam = new HashMap<>();
        regionParam.put("region", region);

        var response = requestService.GET(ApiEndpoints.GET_PROVINCES, regionParam);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public EntityResponse<City> getCities(String province) {
        Map<String, Serializable> provinceParam = new HashMap<>();
        provinceParam.put("province", province);

        var response = requestService.GET(ApiEndpoints.GET_CITIES, provinceParam);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(City.class);
    }

    public EntityResponse<Ad> insertAd(AdInsert ad) {
        String json = new Gson().toJson(ad);
        var response =  requestService.POST(ApiEndpoints.INSERT_AD, SESSION_ID, UserSession.getSessionId(), json);

        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(Ad.class);
    }

    public SimpleResponse uploadImages(int idAd, Photo photo) {
        var oldFileName = photo.getFileName();
        photo.setFileName(idAd + "_" + oldFileName);
        var json = new Gson().toJson(photo);

        var response = requestService.POST(ApiEndpoints.UPLOAD_IMAGE, json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public EntityResponse<Photo> getImages(int idAd) {
        var params = new HashMap<String, Serializable>();
        params.put("idAd", idAd);
        var response =  requestService.GET(ApiEndpoints.GET_IMAGES, params);

        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(Photo.class);
    }

    public EntityResponse<Bid> sendBid(Bid.Insert bid) {
        String json = new Gson().toJson(bid);
        var response = requestService.POST(ApiEndpoints.SEND_BID, SESSION_ID, UserSession.getSessionId(), json);
        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response.parse(Bid.class);
    }

    public EntityResponse<Bid> getBidsBy(String key, Serializable value) {
        var params = new HashMap<String, Serializable>();
        params.put("key", key);
        params.put("value", value);

        var response = requestService.GET(ApiEndpoints.GET_BIDS, params);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response.parse(Bid.class);
    }

    public SimpleResponse cancelBid(int bidId) {
        var params = new HashMap<String, Serializable>();
        params.put("bidId", bidId);
        var response = requestService.PUT(ApiEndpoints.CANCEL_BID, SESSION_ID, UserSession.getSessionId(), params);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public SimpleResponse acceptOrRefuseBid(Bid bid) {
        var json = new Gson().toJson(bid);
        var response = requestService.PUT(ApiEndpoints.ACCEPT_OR_REFUSE_BID, SESSION_ID, UserSession.getSessionId(), json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public SimpleResponse acceptOrRefuseCounterOffer(Bid counteroffer) {
        var json = new Gson().toJson(counteroffer);
        var response = requestService.PUT(ApiEndpoints.ACCEPT_OR_REFUSE_COUNTEROFFER, SESSION_ID, UserSession.getSessionId(), json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public EntityResponse<Ad> searchAds(Ad.SearchBy searchBy) {
        var json = new Gson().toJson(searchBy);
        var response = requestService.PUT(ApiEndpoints.SEARCH_AD, json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response.parse(Ad.class);
    }

}
