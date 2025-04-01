package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.utils.NotificationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgencyRequestsHandler {
    private final AgencyRequestsService agencyRequestsService;
    private final static Logger logger = LoggerFactory.getLogger(AgencyRequestsHandler.class);

    public AgencyRequestsHandler() {
        agencyRequestsService = new AgencyRequestsService();
    }

    public void createAgency(String agency, String vatNumber, String firstName, String lastName, String email, String password) {
        SimpleResponse response = agencyRequestsService.createAgency(
                new RegisterAgencyRequest(
                        agency, vatNumber,
                        new User(firstName, lastName, email, password, agency)
                )
        );


        if (response == null) {
            NotificationFactory.criticalError();
            return;
        }

        if (response.ok()) {
            logger.info("User with email {} new agency: {}", email, agency);
            // send to confirmation page
        }
        else {
            NotificationFactory.error(response.getRawBody());
            logger.warn("User attempted to create agency {} with email: {}", agency, email);
        }
    }
}
