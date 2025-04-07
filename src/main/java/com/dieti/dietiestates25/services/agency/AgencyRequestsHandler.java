package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.logging.Log;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;

public class AgencyRequestsHandler {
    private final AgencyRequestsService agencyRequestsService;

    public AgencyRequestsHandler() {
        agencyRequestsService = new AgencyRequestsService();
    }

    public void createAgency(String agency, String vatNumber, String firstName, String lastName, String email) {
        SimpleResponse response = agencyRequestsService.createAgency(
                new RegisterAgencyRequest(
                        agency, vatNumber,
                        new User(firstName, lastName, email, null, agency)
                )
        );


        if (response == null) {
            NotificationFactory.criticalError();
            return;
        }

        if (response.ok()) {
            Log.info(AgencyRequestsHandler.class, String.format("User with email %s new agency: %s", email, agency));
            NotificationFactory.primary("Now you can login with the temporary password we sent at your email address.");
            UI.getCurrent().navigate(LoginView.class);
        }
        else {
            NotificationFactory.error(response.getRawBody());
            Log.warn(AgencyRequestsHandler.class, String.format("User attempted to create agency %s with email: %s", agency, email));
        }
    }
}
