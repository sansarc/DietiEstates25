package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.BidMessage;
import com.dieti.dietiestates25.utils.TestUtils;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.html.Span;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidsPanelTest {
    
    @Mock
    private AdRequestsHandler handlerMock;
    
    private BidsPanel bidsPanel;
    
    private List<Bid> bids;

    private static Routes routes;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);
        
        bidsPanel = new BidsPanel(TestUtils.mockAd());
        bidsPanel.adRequestsHandler = handlerMock;
        
        bids = createMockBids();
    }

    private List<Bid> createMockBids() {
        // pending bid
        Bid pendingBid = new Bid();
        pendingBid.setId(1);
        pendingBid.setStatus("P");
        pendingBid.setAmount(100000.0);
        pendingBid.setOfferer("johnsmith@dietiestates25.com");
        pendingBid.setFirstname("John");
        pendingBid.setLastname("Smith");

        // accepted bid
        Bid acceptedBid = new Bid();
        acceptedBid.setId(2);
        acceptedBid.setStatus("A");
        acceptedBid.setAmount(120000.0);
        acceptedBid.setOfferer("jane@dietiestates25.com");
        acceptedBid.setFirstname("Jane");
        acceptedBid.setLastname("Doe");

        // Create a refused bid
        Bid refusedBid = new Bid();
        refusedBid.setId(3);
        refusedBid.setStatus("R");
        refusedBid.setAmount(90000.0);
        refusedBid.setOfferer("bob@dietiestates25.com");
        refusedBid.setFirstname("Bob");
        refusedBid.setLastname("Johnson");

        // Create a canceled bid (should not appear in the list)
        Bid canceledBid = new Bid();
        canceledBid.setId(4);
        canceledBid.setStatus("C");
        canceledBid.setAmount(95000.0);
        canceledBid.setFirstname("Alice");
        canceledBid.setLastname("Brown");

        return List.of(pendingBid, acceptedBid, refusedBid, canceledBid);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testCreateBidsList_withBids() {
        when(handlerMock.getBidsBy(eq("ad"), any()))
                .thenReturn(bids);

        bidsPanel.createBidsList(TestUtils.mockAd());

        assertNotNull(BidsPanel.getBidsListLayout());
        assertEquals(3, BidsPanel.getBidsListLayout().getComponentCount());
        assertInstanceOf(BidMessage.class, BidsPanel.getBidsListLayout().getComponentAt(0));
        var acceptedBid = (BidMessage) BidsPanel.getBidsListLayout().getComponentAt(0);
        assertEquals(2, acceptedBid.getBid().getId());
        assertEquals("A", acceptedBid.getBid().getStatus());

        assertTrue(BidsPanel.isOneAccepted());
    }

    @Test
    void testCreateBidsList_withoutBids() {
        when(handlerMock.getBidsBy(eq("ad"), any()))
                .thenReturn(List.of());

        bidsPanel.createBidsList(TestUtils.mockAd());

        assertFalse(_find(bidsPanel, Span.class, spec -> spec.withText("No bids found for this ad.")).isEmpty());
        assertFalse(BidsPanel.isOneAccepted());
    }

    @Test
    void testOnAccepted() {
        when(handlerMock.getBidsBy(eq("ad"), any()))
                .thenReturn(bids);

        bidsPanel.createBidsList(TestUtils.mockAd());

        var toAccept = bids.getFirst();
        toAccept.setStatus("A");

        bidsPanel.onAccepted(toAccept);

        var bidsListLayout = BidsPanel.getBidsListLayout();

        // Since we already had an accepted bid (bid #2), and now bid #1 is also accepted,
        // the accepted bids should be at the top in reverse order of acceptance
        assertInstanceOf(BidMessage.class, bidsListLayout.getComponentAt(0));
        BidMessage firstBidMessage = (BidMessage) bidsListLayout.getComponentAt(0);
        assertEquals(1L, firstBidMessage.getBid().getId());

        bidsListLayout.getChildren()
                .filter(BidMessage.class::isInstance)
                .map(BidMessage.class::cast)
                .forEach(bidMessage -> {
                    assertFalse(bidMessage.getAcceptButton().isEnabled());
                    assertFalse(bidMessage.getRefuseButton().isEnabled());
                });
    }

    @Test
    void testOnRefused() {
        when(handlerMock.getBidsBy(eq("ad"), any()))
                .thenReturn(bids);

        bidsPanel.createBidsList(TestUtils.mockAd());

        var bidMessage = mock(BidMessage.class);
        try (var staticBidMessageMock = mockStatic(BidMessage.class)) {

            var toRefuse = bids.getFirst();
            toRefuse.setStatus("R");

            staticBidMessageMock.when(() -> BidMessage.find(any(), eq(1)))
                    .thenReturn(bidMessage);

            bidsPanel.onRefused(toRefuse);

            // verify setRefused was called on the bid message
            verify(bidMessage).setRefused();
        }
    }

    @Test
    void testOnDeleted() {
        when(handlerMock.getBidsBy(eq("ad"), any()))
                .thenReturn(bids);

        bidsPanel.createBidsList(TestUtils.mockAd());

        var bidsListLayout = BidsPanel.getBidsListLayout();

        var toDelete = bids.getFirst();
        var bidMessage = new BidMessage(toDelete, TestUtils.mockAd().getAgent().getEmail(), bidsPanel);
        bidsListLayout.add(bidMessage);

        int updatedComponentCount = bidsListLayout.getComponentCount();

        try (var staticBidMessageMock = mockStatic(BidMessage.class)) {
            staticBidMessageMock.when(() -> BidMessage.find(any(), eq(1))).thenReturn(bidMessage);

            bidsPanel.onDeleted(toDelete);
            staticBidMessageMock.verify(() -> BidMessage.find(any(), eq(1)));

            assertEquals(updatedComponentCount - 1, bidsListLayout.getComponentCount());
        }
    }
}