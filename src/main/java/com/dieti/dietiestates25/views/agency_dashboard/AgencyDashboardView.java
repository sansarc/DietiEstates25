package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.dto.Agency;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.utils.BadgeFactory;
import com.dieti.dietiestates25.views.login.LoginView;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.dieti.dietiestates25.views.profile.ProfileView;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.InfoPopover;
import com.dieti.dietiestates25.ui_components.TextWithLink;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;

@Route(value = "agency-dashboard", layout = MainLayout.class)
@RouteAlias(value = "agency-dashboard/:agency", layout = MainLayout.class)
public class AgencyDashboardView extends VerticalLayout  implements BeforeEnterObserver {

    transient AgencyRequestsHandler agencyRequestsHandler = new AgencyRequestsHandler();

    DivContainer container;
    VerticalLayout agentsLayout;

    private static final Map<String, Agency> TEMP_AGENCY_CACHE = new HashMap<>();

    public static void cacheAgency(Agency agency) {
        TEMP_AGENCY_CACHE.put(agency.getName(), agency);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var agencyParam = event.getRouteParameters().get("agency");
        Agency agency;

        if (agencyParam.isPresent() && !agencyParam.get().equals(UserSession.getAgencyName())) {
            var agencyName = agencyParam.get();

            if (TEMP_AGENCY_CACHE.containsKey(agencyName)) {
                agency = TEMP_AGENCY_CACHE.get(agencyName);
                configureComponents(agency, false);
            }
            else {
                event.forwardTo(PageNotFoundView.class);
                return;
            }
        }
        else {
            if (!UserSession.isManagerOrAgent())
                event.forwardTo(LoginView.class);

            agency = new Agency(UserSession.getAgencyName(), UserSession.getAgencyVAT());
            configureComponents(agency, true);
        }

        configureLayout();

        UI.getCurrent().access(() ->
                UI.getCurrent().getPage().setTitle(agency.getName())
        );
    }

    public AgencyDashboardView() {/* Components configuration in beforeEnter */}

    private void configureComponents(Agency agency, boolean isPersonalAgency) {

        container = new DivContainer("600px", "auto");
        var agencyTitle = new H1(agency.getName());

        createAgentsDetails(agency, isPersonalAgency);

        container.add(agencyTitle);
        add(container, agentsLayout);
    }


    private void createAgentsDetails(Agency agency, boolean isPersonalAgency) {
        var addAgentButton = new Button(VaadinIcon.PLUS.create(), event -> new AddAgentDialog().open());
        addAgentButton.getStyle().setCursor("pointer");
        new InfoPopover(addAgentButton, "Add a new agent");

        var title = new H3("Agents");
        title.getStyle().setCursor("pointer");

        var titleLayout = new HorizontalLayout(title);
        if (isPersonalAgency && UserSession.isManager())
            titleLayout.add(addAgentButton);
        titleLayout.setAlignItems(Alignment.CENTER);

        agentsLayout = new VerticalLayout(titleLayout);
        var agentsList = agencyRequestsHandler.getAgents(agency.getVatNumber());

        if (agentsList != null) {

            if (isPersonalAgency && UserSession.isManager())
                agentsList.removeFirst(); // remove the manager from agents list when user's actually the manager.

            if (agentsList.isEmpty())
                createEmptyAgentListView();
            else {
                for (var agent : agentsList)
                    createAgentCard(agent);
            }
        }
    }

    private void createEmptyAgentListView() {
        var addAgentAnchor = new Anchor("#", "here");
        addAgentAnchor.getElement().addEventListener("click", event ->
                new AddAgentDialog().open()
        ).addEventData("event.preventDefault()");

        var emptyAgents = new TextWithLink("Looks like there are no agents signed up yet. Add them", addAgentAnchor, ".");
        emptyAgents.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
        agentsLayout.add(emptyAgents);
    }

    private void createAgentCard(User agent) {
        var agentName = agent.getFirstName() + " " + agent.getLastName();
        var agentAvatar = new Avatar(agentName);
        var agentLink = new RouterLink(agentName, ProfileView.class, new RouteParameters("email", agent.getEmail()));
        agentLink.getElement().addEventListener("click", event -> ProfileView.cacheUser(agent));

        var layout = new HorizontalLayout(agentAvatar, agentLink);
        layout.setAlignItems(Alignment.CENTER);

        if (agent.getRole().equals("M")) {
            var managerBadge = new Span(BadgeFactory.createTitle("Manager"));
            managerBadge.getElement().getThemeList().add("badge primary");
            layout.add(managerBadge);
        }

        var agentDiv = new DivContainer("auto", "30px");
        agentDiv.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        agentDiv.add(layout);

        agentsLayout.add(agentDiv);
    }

    private void configureLayout() {
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        agentsLayout.setWidth("80%");

        container.getStyle().setAlignItems(Style.AlignItems.CENTER);
    }

}
