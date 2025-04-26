package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.services.logging.Log;
import com.dieti.dietiestates25.utils.DialogUtils;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.dieti.dietiestates25.views.login.LoginView;
import com.vaadin.flow.component.UI;

import java.util.List;

public class AgencyRequestsHandler {

    private final AgencyRequestsService agencyRequestsService;

    public AgencyRequestsHandler() {
        agencyRequestsService = new AgencyRequestsService();
    }

    public void createAgency(String agencyName, String vatNumber, String firstName, String lastName, String email) {
        var agency = new RegisterAgency(
                agencyName, vatNumber,
                new User(firstName, lastName, email, null, agencyName)
        );

        var response = agencyRequestsService.createAgency(agency);

        if (response == null) {
            
            return;
        }

        if (response.ok()) {
            Log.info(AgencyRequestsHandler.class, String.format("User with email %s new agency: %s", email, agencyName));
            NotificationFactory.primary("Login with the temporary password we sent at " + email + ".");
            UI.getCurrent().navigate(LoginView.class);
        }
        else {
            NotificationFactory.error(response.getRawBody());
            Log.warn(AgencyRequestsHandler.class, String.format("User attempted to create agency %s with email: %s", agencyName, email));
        }
    }

    public void confirmManagerOrAgentAccount(String email, String oldPwd, String newPwd) {
        SimpleResponse confirmed = agencyRequestsService.confirmManagerOrAgentAccount(email, oldPwd, newPwd);

        if (confirmed == null) {
            
            return;
        }

        if (confirmed.ok()) {
            if (UserSession.isManager())
                NotificationFactory.success("You have successfully confirmed your account and created " + UserSession.getAgencyName() + "!");
            if (UserSession.isAgent())
                NotificationFactory.success("You have successfully confirmed your account!");

            Log.info(AgencyRequestsHandler.class,"User changed temp password with email: " + email);
            DialogUtils.closeOpenDialogs();
        } else {
            NotificationFactory.error(confirmed.getRawBody());
            Log.warn(AgencyRequestsHandler.class, "User wasn't able to change temp password with email: " + email);
        }
    }

    public void createAgent(String firstName, String lastName, String email) {
        var user = new User(firstName, lastName, email);
        var response = agencyRequestsService.createAgent(user);

        if (response == null) {
            
            return;
        }

        if (response.ok()) {
            NotificationFactory.primary("The new agent will receive an email with the instructions for confirming their account.");
            Log.info(AgencyRequestsHandler.class, "Manager " + UserSession.getEmail() + " created a new agent: " + email);
            DialogUtils.closeOpenDialogs();
        }
        else {
            NotificationFactory.error(response.getRawBody());
            Log.warn(AgencyRequestsHandler.class, "User " + UserSession.getEmail() + " attempted to create an agent");
        }
    }

    public List<User> getAgents(String agencyVAT) {
        EntityResponse<User> response = agencyRequestsService.getAgents(agencyVAT);

        if (response == null)
            return null;

        if (response.ok())
            return response.getEntities();

        else {
            NotificationFactory.error(response.getRawBody());
            Log.warn(AgencyRequestsHandler.class, UserSession.getEmail() + " couldn't get agents.");
            return null;
        }
    }

}
