package com.dieti.dietiestates25.views.profile;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.AdCard;
import com.dieti.dietiestates25.ui_components.BidMessage;
import com.dieti.dietiestates25.utils.TestUtils;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileViewTest {

    private static final User TEST_USER = TestUtils.newUser("A");
    private static final User OTHER_USER = TestUtils.newUser("U");

    private ProfileView view;

    @Mock
    private AdRequestsHandler handlerMock;

    @Mock
    private BeforeEnterEvent beforeEnterMock;  // handles component rendering based on the query parameters

    private static Routes routes;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);

        view = new ProfileView();
        view.adRequestsHandler = handlerMock;

        UserSession.init(TEST_USER);    // mocking a session to bypass @ForwardGuest and avoid doing it statically for each test
        UserSession.setSessionId("mock-session");
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
    void keyComponentsShouldBeInitialized_personalProfile() {
        when(handlerMock.getAdsByAgent(any()))
                .thenReturn(new ArrayList<>());

        when(handlerMock.getBidsBy(eq("OFFERER"), any()))
                .thenReturn(new ArrayList<>());

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(new HashMap<>()));

        view.beforeEnter(beforeEnterMock);

        // verifying UI components
        assertEquals(TEST_USER.getFirstName() + " " + TEST_USER.getLastName(), view.container.getComponentAt(1).getElement().getText());
        assertEquals(TEST_USER.getEmail(), view.container.getComponentAt(2).getElement().getText());

        boolean hasBids = view.getChildren()
                .anyMatch(component -> component instanceof Span &&
                        ((Span) component).getText().contains("Looks like you haven't place a bid yet"));
        assertTrue(hasBids);

        verify(handlerMock, atMostOnce()).getAdsByAgent(any());
        verify(handlerMock, atMostOnce()).getBidsBy(eq("offerer"), any());
    }

    @Test
    void keyComponentsShouldBeInitialized_otherUserProfile() {
        ProfileView.cacheUser(OTHER_USER);

        when(handlerMock.getAdsByAgent(any()))
                .thenReturn(new ArrayList<>());

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("email", OTHER_USER.getEmail()));

        view.beforeEnter(beforeEnterMock);

        assertEquals(OTHER_USER.getFirstName() + " " + OTHER_USER.getLastName(),
                view.container.getComponentAt(1).getElement().getText());
        assertEquals(OTHER_USER.getEmail(), view.container.getComponentAt(2).getElement().getText());

        verify(handlerMock, atMostOnce()).getAdsByAgent(any());
        verify(handlerMock, atMostOnce()).getBidsBy(eq("offerer"), any());
    }

    @Test
    void userNotFoundShouldForwardToPageNotFoundView() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("email", "notFound@example.com"));

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock, atMostOnce()).forwardTo(PageNotFoundView.class);
        verifyNoInteractions(handlerMock);
    }

    @Test
    void testAgentPersonalProfile() {
        var mockAds = TestUtils.mockAdsList();
        var mockBids = TestUtils.mockBidsList();

        when(handlerMock.getAdsByAgent(any()))
                .thenReturn(mockAds);

        when(handlerMock.getBidsBy(eq("OFFERER"), any()))
                .thenReturn(mockBids);

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters(new HashMap<>()));

        view.beforeEnter(beforeEnterMock);

        assertTrue(
                view.getChildren()
                        .anyMatch(component -> component instanceof H3 &&
                                ((H3) component).getText().contains("Ads"))
        );

        assertTrue(
                view.getChildren()
                        .anyMatch(component -> component instanceof H3 &&
                                ((H3) component).getText().contains("Bids you've placed."))
        );

        // verify that there are both ads' and bids' scroller components
        assertEquals(2,
                view.getChildren()
                        .filter(Scroller.class::isInstance)
                        .count()
        );

        // ads list assertions
        assertEquals(2, view.adsList.getComponentCount());
        assertEquals(1, ((AdCard) ((HorizontalLayout) view.adsList.getComponentAt(0)).getComponentAt(0)).getAd().getId());
        assertEquals(2, (((AdCard) ((HorizontalLayout) view.adsList.getComponentAt(1)).getComponentAt(0)).getAd().getId()));


        int[] expectedBidIds = {1, 2, 3}; // expected ids
        int[] componentBidIds = new int[view.bidsList.getComponentCount()];  // ids from bidMessage components
        for (int i = 0; i < view.bidsList.getComponentCount(); i++)
            componentBidIds[i] = ((BidMessage) view.bidsList.getComponentAt(i)).getBid().getId();

        // apparently components in bidsList are not always listed in the same order
        Arrays.sort(expectedBidIds);
        Arrays.sort(componentBidIds);

        // asserting that both arrays are equal regardless of order
        assertArrayEquals(expectedBidIds, componentBidIds);

        verify(handlerMock, atMostOnce()).getAdsByAgent(any());
        verify(handlerMock, atMostOnce()).getBidsBy(eq("OFFERER"), any());
    }

    @Test
    void testBidDeletion() {
        var bid = TestUtils.mockBidsList().getFirst();

        var bidMessageMock = new BidMessage();
        bidMessageMock.setBid(bid);

        view.bidsList = new VerticalLayout(bidMessageMock);

        try (var bidMessageStaticMock = mockStatic(BidMessage.class)) {
            bidMessageStaticMock.when(() -> BidMessage.find(any(), eq(bid.getId())))
                    .thenReturn(bidMessageMock);

            assertEquals(1, view.bidsList.getComponentCount());
            view.onDeleted(bid);
            assertEquals(0, view.bidsList.getComponentCount()); // => bidMessage correctly removed
        }
    }
}