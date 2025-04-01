package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.constants.Constants.*;
import com.dieti.dietiestates25.dto.Bid;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AdRequestsService {

    public SimpleResponse getRegions() {
        return RequestService.GET(ApiEndpoints.GET_REGIONS).checkNullityBeforeReturning();
    }

    public SimpleResponse getProvinces(String region) {
        Map<String, String> regionParam = new HashMap<>();
        regionParam.put("region", region);

        var response = RequestService.GET(ApiEndpoints.GET_PROVINCES, regionParam);
        return response.checkNullityBeforeReturning();
    }

    public SimpleResponse getCities(String province) {
        Map<String, String> provinceParam = new HashMap<>();
        provinceParam.put("province", province);

        var response = RequestService.GET(ApiEndpoints.GET_CITIES, provinceParam);
        return response.checkNullityBeforeReturning();
    }

    public SimpleResponse insertAd(Ad ad) {
        String json = new Gson().toJson(ad);
        return RequestService.POST(ApiEndpoints.INSERT_AD, json);
    }

    public SimpleResponse sendBid(Bid bid) {
        String json = new Gson().toJson(bid);
        return RequestService.POST(ApiEndpoints.SEND_BID, json);
    }
}
