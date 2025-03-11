package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.RegisterAgencyRequest;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.dto.Signup;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgencyRequestsHandler {
    private final AgencyRequestsService agencyRequestsService;
    private final static Logger logger = LoggerFactory.getLogger(AgencyRequestsHandler.class);

    public AgencyRequestsHandler() {
        agencyRequestsService = new AgencyRequestsService();
    }

    public void createAgency(String agency, String vatNumber, String firstName, String lastName, String email, String password) {
        Response response = agencyRequestsService.createAgency(
                new RegisterAgencyRequest(
                        agency, vatNumber,
                        new Signup(firstName, lastName, email, password)
                )
        );

        VaadinSession session = VaadinSession.getCurrent();

        try {
            if (session == null) {
                session = new VaadinSession(VaadinService.getCurrent());
                VaadinSession.setCurrent(session);
            }
            if (response.getStatusCode() == Constants.Codes.CREATED) {
                session.setAttribute("email", email);
                logger.info("User with email {} new agency: {}", email, agency);
            }
            else {
                NotificationFactory.error(response.getMessage());
                logger.warn("User attempted to create agency {} with email: {}", agency, email);
            }
        } catch (RuntimeException e) {
            logger.error("Crical error while creating agency.", e);
            throw new RuntimeException(e);
        }
    }
}
