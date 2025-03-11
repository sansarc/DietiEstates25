package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.dto.ad.AdRequest;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.Gson;

public class AdRequestsService {
    public Response insertAd(AdRequest ad) {
        String json = new Gson().toJson(ad);
        return RequestService.POST(Constants.ApiEndpoints.INSERT_AD, json);
    }
}
