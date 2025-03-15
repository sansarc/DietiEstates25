package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdRequestsHandler {
    private final AdRequestsService adRequestsService;
    private final static Logger logger = LoggerFactory.getLogger(AdRequestsHandler.class);

    public AdRequestsHandler() { adRequestsService = new AdRequestsService(); }

    public void insertAd(Ad ad) {
        Response response = adRequestsService.insertAd(ad);

        if (response.getStatusCode() == Constants.Codes.CREATED) {
            logger.info("New ad created by {}", VaadinSession.getCurrent().getAttribute("email"));
            NotificationFactory.success(response.getMessage());
        } else {
            logger.warn("Failed to create ad for user {}", VaadinSession.getCurrent().getAttribute("email"));
            NotificationFactory.error(response);
        }
    }
}
