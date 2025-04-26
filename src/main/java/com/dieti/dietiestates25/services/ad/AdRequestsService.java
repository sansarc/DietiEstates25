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
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.dieti.dietiestates25.constants.Constants.Codes.INTERNAL_SERVER_ERROR;

public class AdRequestsService {

    public SimpleResponse getRegions() {
        var response = RequestService.GET(ApiEndpoints.GET_REGIONS);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse getProvinces(String region) {
        Map<String, Serializable> regionParam = new HashMap<>();
        regionParam.put("region", region);

        var response = RequestService.GET(ApiEndpoints.GET_PROVINCES, regionParam);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public EntityResponse<City> getCities(String province) {
        Map<String, Serializable> provinceParam = new HashMap<>();
        provinceParam.put("province", province);

        var response = RequestService.GET(ApiEndpoints.GET_CITIES, provinceParam);
        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(City.class);
    }

    public EntityResponse<Ad> insertAd(AdInsert ad) {
        String json = new Gson().toJson(ad);
        var response =  RequestService.POST(ApiEndpoints.INSERT_AD, "sessionId", UserSession.getSessionId(), json);

        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(Ad.class);
    }

    public SimpleResponse uploadImages(int idAd, Photo photo) {
        var oldFileName = photo.getFileName();
        photo.setFileName(idAd + "_" + oldFileName);
        var json = new Gson().toJson(photo);

        var response = RequestService.POST(ApiEndpoints.UPLOAD_IMAGE, json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public EntityResponse<Ad> getAd(int idAd) {
        var json = new Gson().toJson(new Ad.IdOnly(idAd));
        var response = RequestService.PUT(ApiEndpoints.SEARCH_AD, json);

        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(Ad.class);
    }

    public EntityResponse<Photo> getImages(int idAd) {
        var params = new HashMap<String, Serializable>();
        params.put("idAd", idAd);
        var response =  RequestService.GET(ApiEndpoints.GET_IMAGES, params);

        if (response.getStatusCode() == INTERNAL_SERVER_ERROR)
            return null;

        return response.parse(Photo.class);
    }

    public EntityResponse<Bid> sendBid(Bid.Insert bid) {
        String json = new Gson().toJson(bid);
        var response = RequestService.POST(ApiEndpoints.SEND_BID, "sessionId", UserSession.getSessionId(), json);
        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response.parse(Bid.class);
    }

    public EntityResponse<Bid> getBidsBy(String key, Serializable value) {
        var params = new HashMap<String, Serializable>();
        params.put("key", key);
        params.put("value", value);

        var response = RequestService.GET(ApiEndpoints.GET_BIDS, params);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response.parse(Bid.class);
    }

    public SimpleResponse cancelBid(int bidId) {
        var params = new HashMap<String, Serializable>();
        params.put("bidId", bidId);
        var response = RequestService.PUT(ApiEndpoints.CANCEL_BID, "sessionId", UserSession.getSessionId(), params);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public SimpleResponse acceptOrRefuseBid(Bid bid) {
        var json = new Gson().toJson(bid);
        var response = RequestService.PUT(ApiEndpoints.ACCEPT_OR_REFUSE_BID, "sessionId", UserSession.getSessionId(), json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

    public SimpleResponse acceptOrRefuseCounterOffer(Bid counteroffer) {
        var json = new GsonBuilder().create().toJson(counteroffer);
        var response = RequestService.PUT(ApiEndpoints.ACCEPT_OR_REFUSE_COUNTEROFFER, "sessionId", UserSession.getSessionId(), json);

        return response.getStatusCode() == INTERNAL_SERVER_ERROR ? null : response;
    }

}
