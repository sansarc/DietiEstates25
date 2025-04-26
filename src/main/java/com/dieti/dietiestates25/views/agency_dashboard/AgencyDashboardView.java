package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.dto.Agency;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.dieti.dietiestates25.views.profile.Profile;
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

import java.util.HashMap;
import java.util.Map;

@Route(value = "agency-dashboard", layout = MainLayout.class)
@RouteAlias(value = "agency-dashboard/:agency", layout = MainLayout.class)
public class AgencyDashboardView extends VerticalLayout  implements BeforeEnterObserver {

    final AgencyRequestsHandler agencyRequestsHandler = new AgencyRequestsHandler();

    DivContainer container;
    Details agentsDetails;

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
            agency = new Agency(UserSession.getAgencyName(), UserSession.getAgencyVAT());
            configureComponents(agency, true);
        }

        configureLayout();

        UI.getCurrent().access(() ->
                UI.getCurrent().getPage().setTitle(agency.getName())
        );
    }

    public AgencyDashboardView() {}

    private void configureComponents(Agency agency, boolean isPersonalAgency) {

        container = new DivContainer("600px", "auto");
        var agencyTitle = new H1(agency.getName());

        var ads = new Span("Looks like " + agency.getName() + " hasn't uploaded any ad yet.");
        ads.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
        var adsDetails = new Details(new H3("Ads"), createContent(ads));
        adsDetails.setWidth("80%");

        var bids = new Span("Looks like " + agency.getName() + " hasn't received any bid yet.");
        bids.getStyle().set("color", "#888888").setPaddingTop("var(--lumo-space-s)");
        var bidsDetails = new Details(new H3("Bids"), createContent(bids));
        bidsDetails.setWidth("80%");

        agentsDetails = new Details();
        createAgentsDetails(agency, isPersonalAgency);

        container.add(agencyTitle);
        add(container, adsDetails, bidsDetails, agentsDetails);
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

        var agentsList = agencyRequestsHandler.getAgents(agency.getVatNumber());
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
                if (isPersonalAgency && UserSession.isManager())
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
