package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.DialogUtils;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AgencyRequestsHandler {

    AgencyRequestsService agencyRequestsService;
    private static final Logger logger = LoggerFactory.getLogger(AgencyRequestsHandler.class);

    public AgencyRequestsHandler() {
        agencyRequestsService = new AgencyRequestsService();
    }

    public void createAgency(String agencyName, String vatNumber, String firstName, String lastName, String email) {
        var agency = new RegisterAgency(
                agencyName, vatNumber,
                new User(firstName, lastName, email, null, agencyName)
        );

        var response = agencyRequestsService.createAgency(agency);

        if (response == null) return;

        if (response.ok()) {
            logger.info("User with email {} new agency: {}", email, agencyName);
            NotificationFactory.primary("Login with the temporary password we sent at " + email + ".");
            UI.getCurrent().navigate(LoginView.class);
        }
        else {
            NotificationFactory.error(response.getRawBody());
            logger.warn("User attempted to create agency {} with email: {}", agencyName, email);
        }
    }

    public void confirmManagerOrAgentAccount(String email, String oldPwd, String newPwd) {
        SimpleResponse confirmed = agencyRequestsService.confirmManagerOrAgentAccount(email, oldPwd, newPwd);

        if (confirmed == null) return;

        if (confirmed.ok()) {
            if (UserSession.isManager())
                NotificationFactory.success("You have successfully confirmed your account and created " + UserSession.getAgencyName() + "!");
            if (UserSession.isAgent())
                NotificationFactory.success("You have successfully confirmed your account!");

            logger.info("User changed temp password with email: {}", email);
            DialogUtils.closeOpenDialogs();
        } else {
            NotificationFactory.error(confirmed.getRawBody());
            logger.warn("User wasn't able to change temp password with email: {}", email);
        }
    }

    public void createAgent(String firstName, String lastName, String email) {
        var user = new User(firstName, lastName, email);
        var response = agencyRequestsService.createAgent(user);

        if (response == null)
            return;

        if (response.ok()) {
            NotificationFactory.primary("The new agent will receive an email with the instructions for confirming their account.");
            logger.info("New agent created: {}", email);
            DialogUtils.closeOpenDialogs();
        }
        else {
            NotificationFactory.error(response.getRawBody());
            logger.warn("Agent creation failed.");
        }
    }

    public List<User> getAgents(String agencyVAT) {
        EntityResponse<User> response = agencyRequestsService.getAgents(agencyVAT);

        if (response == null)
            return null;

        return response.getEntities();
    }

}