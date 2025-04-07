package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.Gson;

public class AgencyRequestsService {

    public SimpleResponse createAgency(RegisterAgencyRequest agency) {
        String json = new Gson().toJson(agency);
        var response = RequestService.POST(Constants.ApiEndpoints.CREATE_AGENCY, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }
}
