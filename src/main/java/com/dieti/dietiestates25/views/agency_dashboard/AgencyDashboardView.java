package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.views.profile.Profile;
import com.dieti.dietiestates25.annotations.roles_only.ManagerOrAgentOnly;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.InfoPopover;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

import java.util.Optional;

@ManagerOrAgentOnly
@Route(value = "agency-dashboard", layout = MainLayout.class)
public class AgencyDashboardView extends VerticalLayout {

    final AgencyRequestsHandler agencyRequestsHandler = new AgencyRequestsHandler();

    DivContainer container;
    Details agentsDetails;

    String agencyName = Optional.ofNullable(UserSession.getAgency()).orElse("Agency");

    public AgencyDashboardView() {
        UI.getCurrent().access(() ->
                UI.getCurrent().getPage().setTitle(agencyName + " | Dashboard")
        );
        configureComponents();
        configureLayout();
    }

    private void configureComponents() {

        container = new DivContainer("600px", "auto");
        var agencyTitle = new H1(agencyName);

        var ads = new Span("Looks like " + agencyName + " hasn't uploaded any ad yet.");
        ads.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
        var adsDetails = new Details(new H3("Ads"), createContent(ads));
        adsDetails.setWidth("80%");

        var bids = new Span("Looks like " + agencyName + " hasn't received any bid yet.");
        bids.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
        var bidsDetails = new Details(new H3("Bids"), createContent(bids));
        bidsDetails.setWidth("80%");

        agentsDetails = new Details();
        createAgentsDetails();

        container.add(agencyTitle);
        add(container, adsDetails, bidsDetails, agentsDetails);
    }

    private void createAgentsDetails() {
        var addAgentButton = new Button(VaadinIcon.PLUS.create(), event -> new AddAgentDialog().open());
        addAgentButton.getStyle().setCursor("pointer");
        new InfoPopover(addAgentButton, "Add a new agent");

        var title = new H3("Agents");
        title.getStyle().setCursor("pointer");

        var titleLayout = new HorizontalLayout(title);
        if (UserSession.isManager()) titleLayout.add(addAgentButton);
        titleLayout.setAlignItems(Alignment.CENTER);

        var agentsList = agencyRequestsHandler.getAgents(UserSession.getAgencyVAT());
        if (agentsList != null) {

            if (agentsList.isEmpty()) {
                var addAgentAnchor = new Anchor("", "here");
                addAgentAnchor.getElement().addEventListener("click", event ->
                        new AddAgentDialog().open()
                ).addEventData("event.preventDefault()");

                var emptyAgents = new TextWithLink("Looks like there are no agents signed up yet. Add them", addAgentAnchor, ".");
                emptyAgents.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
                agentsDetails = new Details(titleLayout, createContent(emptyAgents));
                agentsDetails.setOpened(true);
            }

            else {
                if (UserSession.isManager() && UserSession.getAgency().equals(agencyName))
                    agentsList.remove(0); // remove the manager from agents list when user's actually the manager.

                var agentsLayout = new VerticalLayout();
                for (var agent : agentsList) {
                    var agentName = agent.getFirstName() + " " + agent.getLastName();
                    var agentAvatar = new Avatar(agentName);
                    var agentLink = new RouterLink(agentName, Profile.class, new RouteParameters("email", agent.getEmail()));
                    agentLink.getElement().addEventListener("click", event -> Profile.cacheUser(agent));
                    var agentDiv = new DivContainer("auto", "30px");
                    agentDiv.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
                    var layout = new HorizontalLayout(agentAvatar, agentLink);
                    layout.setAlignItems(Alignment.CENTER);
                    agentDiv.add(layout);
                    agentsLayout.add(agentDiv);
                }
                agentsDetails = new Details(titleLayout, agentsLayout);
            }
        }
    }

    private VerticalLayout createContent(Component...components) {
        var content = new VerticalLayout(components);
        content.setPadding(false);
        content.setSpacing(false);
        return content;
    }

    private void configureLayout() {
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        agentsDetails.setWidth("80%");

        container.getStyle().setAlignItems(Style.AlignItems.CENTER);
    }

}
