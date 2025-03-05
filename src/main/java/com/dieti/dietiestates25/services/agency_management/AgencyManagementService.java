package com.dieti.dietiestates25.services.agency_management;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.Gson;

public class AgencyManagementService {

    public Response createAgency(RegisterAgencyRequest agency) {
        String json = new Gson().toJson(agency);

        return RequestService.POST(Constants.ApiEndpoints.CREATE_AGENCY, json);
    }

}
