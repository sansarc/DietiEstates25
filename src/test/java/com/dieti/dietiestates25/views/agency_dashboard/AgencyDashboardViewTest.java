package com.dieti.dietiestates25.views.agency_dashboard;

import com.dieti.dietiestates25.dto.Agency;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.agency.AgencyRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.TestUtils;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgencyDashboardViewTest {

    private static final User MANAGER = TestUtils.newUser("M");
    private static final User AGENT = TestUtils.newUser("A");
    private static final User OTHER_AGENT = TestUtils.newUser("A");

    private static final Agency XYZ_AGENCY = new Agency("XYZ Agency", "987654321");

    private AgencyDashboardView view;

    @Mock
    private AgencyRequestsHandler handlerMock;

    @Mock
    private BeforeEnterEvent beforeEnterMock;

    private static Routes routes;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);

        view = new AgencyDashboardView();
        view.agencyRequestsHandler = handlerMock;

        MockedUI.getCurrent().add(view);
    }

    @AfterEach
    void tearDown() {
        UserSession.clearSession();
        MockVaadin.tearDown();
    }

    /*
     * Components are initialized and rendered from beforeEvent implementation
     * and not from the constructor like in other views
     */

    @Test
    void testManagerViewsOwnAgency() {
        UserSession.init(MANAGER);
        UserSession.setSessionId("mock-session");

        var agents = List.of(MANAGER, AGENT);

        when(handlerMock.getAgents(any()))
                .thenReturn(agents);

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(Map.of()));

        view.beforeEnter(beforeEnterMock);

        assertEquals("ACME Agency", ((H1) view.container.getComponentAt(0)).getText());

        // verifying that add agent button is there
        assertNotNull(view.addAgentButton);

        // verifying that the number of shown agents in the layout is actually the same as the List
        view.agentsLayout.getComponentAt(0).removeFromParent();
        assertEquals(agents.size(), view.agentsLayout.getComponentCount());

        // `agentsLayout` (that has agents.size() number of Divs) -> `agentDiv` (that has only HorizontalLayout `layout` component)
        // -> `layout` (that has a RouterLink as a second component) -> RouterLink -> getText()
        var agentNames = view.agentsLayout.getChildren()
                .map(div -> div.getChildren()
                        .filter(HorizontalLayout.class::isInstance)
                        .map(HorizontalLayout.class::cast)
                        .findFirst()
                        .map(layout -> {
                            RouterLink link = (RouterLink) layout.getComponentAt(1); // index 1 is RouterLink
                            return link.getText();
                        }).orElseThrow(() -> new AssertionError("No layout or RouterLink found"))
                ).toList();

        var expected = agents.stream()
                .map(agent -> agent.getFirstName() + " " + agent.getLastName())
                .toList();

        assertEquals(expected, agentNames);
    }

    @Test
    void testAgentViewsOwnAgency() {
        UserSession.init(AGENT);
        UserSession.setSessionId("mock-session");

        var agents = List.of(MANAGER, AGENT);

        when(handlerMock.getAgents(any()))
                .thenReturn(agents);

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(Map.of()));

        view.beforeEnter(beforeEnterMock);

        // things are the same except the add button that should NOT be there
        assertNull(view.addAgentButton);
    }

    @Test
    void testAgentViewsOtherAgency() {
        UserSession.init(AGENT);
        UserSession.setSessionId("mock-session");

        var agents = List.of(OTHER_AGENT);

        when(handlerMock.getAgents(XYZ_AGENCY.getVatNumber()))
                .thenReturn(agents);

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(Map.of("agency", XYZ_AGENCY.getName())));

        AgencyDashboardView.cacheAgency(XYZ_AGENCY);
        view.beforeEnter(beforeEnterMock);

        assertEquals("XYZ Agency", ((H1) view.container.getComponentAt(0)).getText());
        assertTrue(_find(Button.class, spec -> spec.withIcon(VaadinIcon.PLUS)).isEmpty());
        view.agentsLayout.getComponentAt(0).removeFromParent(); // removing the H3 so to match the agents size
        assertEquals(agents.size(), view.agentsLayout.getComponentCount());
    }

    @Test
    void testAgencyNotFound() {
        UserSession.init(AGENT);
        UserSession.setSessionId("mock-session");

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(Map.of("agency", "notFound")));

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock, atMostOnce()).forwardTo(PageNotFoundView.class);
        verifyNoInteractions(handlerMock);
    }

    @Test
    void testNoQueryParameters_withStandardUser_shouldForwardToPageNotFoundView() {
        UserSession.init(TestUtils.newUser("U"));
        UserSession.setSessionId("mock-session");

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(Map.of()));

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock, atMostOnce()).forwardTo(PageNotFoundView.class);
        verifyNoInteractions(handlerMock);
    }

}