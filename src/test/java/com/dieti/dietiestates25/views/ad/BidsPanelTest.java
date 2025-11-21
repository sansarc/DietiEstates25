package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.observers.BidActionListener;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.BidMessage;
import com.dieti.dietiestates25.utils.TestUtils;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.ButtonKt._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
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
        
        bids = TestUtils.mockBidsList();
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testCreateBidsList_withBids() {
        when(handlerMock.getBidsBy(eq("AD"), any()))
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
        when(handlerMock.getBidsBy(eq("AD"), any()))
                .thenReturn(List.of());

        bidsPanel.createBidsList(TestUtils.mockAd());

        assertFalse(_find(bidsPanel, Span.class, spec -> spec.withText("No bids found for this ad.")).isEmpty());
        assertFalse(BidsPanel.isOneAccepted());
    }

    @Test
    void testOnAccepted() {
        when(handlerMock.getBidsBy(eq("AD"), any()))
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
        when(handlerMock.getBidsBy(eq("AD"), any()))
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
        when(handlerMock.getBidsBy(eq("AD"), any()))
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

    @Test
    void testAcceptButtonCallsHandlerAndListener() {
        var handlerMock = mock(AdRequestsHandler.class);
        var listenerMock = mock(BidActionListener.class);
        var bid = bids.getFirst();

        when(handlerMock.acceptOrRefuseBid(any())).thenReturn(true);

        var msg = new BidMessage(bid, "x@test.com", listenerMock);
        msg.adRequestsHandler = handlerMock;

        _click(msg.getAcceptButton());

        verify(handlerMock).acceptOrRefuseBid(any());
        verify(listenerMock).onAccepted(bid);
    }

    private static Bid.Counteroffer getCounteroffer(String status) {
        var counter = new Bid.Counteroffer();
        counter.setId(5);
        counter.setStatus(status);
        counter.setAmount(1000);
        return counter;
    }

    @Test
    void testCounterOfferAcceptedBranch() {
        var counter = getCounteroffer("A");

        var bid = bids.getFirst();
        bid.setCounteroffer(counter);

        var msg = new BidMessage(bid, bid.getOfferer(), mock(BidActionListener.class));

        var offerSpan = msg.getCounterOfferLayout().getComponentAt(0);
        assertEquals("green", offerSpan.getStyle().get("color"));
    }

    @Test
    void testCounterOfferRefusedBranch() {
        var counter = getCounteroffer("R");

        var bid = bids.getFirst();
        bid.setCounteroffer(counter);

        var msg = new BidMessage(bid, bid.getOfferer(), mock(BidActionListener.class));

        // The accept/refuse anchors shouldn't be present
        assertFalse(msg.getCounterOfferLayout().getComponentAt(0).getChildren().anyMatch(Anchor.class::isInstance));
    }

    @Test
    void testCounterOfferPendingShowsAnchorButtons() {
        var counter = getCounteroffer("P");
        var bid = bids.getFirst();

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getEmail).thenReturn("AGENT@example.com");

            bid.setOfferer(UserSession.getEmail());
            bid.setCounteroffer(counter);

            var msg = new BidMessage(bid, bid.getOfferer(), mock(BidActionListener.class));

            assertEquals(2, msg.getCounterOfferLayout().getChildren()
                    .filter(Anchor.class::isInstance)
                    .count());
        }
    }

    @Test
    void testLaunchCounterOfferDialogStraightRefusalCallsListener() {
        // ... (Mocks and setup remain the same) ...
        var handlerMock = mock(AdRequestsHandler.class);
        var listenerMock = mock(BidActionListener.class);
        when(handlerMock.acceptOrRefuseBid(any())).thenReturn(true);

        var bid = bids.getFirst();
        var msg = new BidMessage(bid, bid.getOfferer(), listenerMock);
        msg.adRequestsHandler = handlerMock;

        // 1. Open the dialog
        _click(msg.getRefuseButton());

        // 2. Find the Dialog
        // Dialogs are attached to the UI root, so we use _get() without a parent
        // to search the whole UI. We search for 'Dialog', not 'ConfirmDialog'.
        var dialog = _get(Dialog.class);

        // 3. Find the "Confirm" button inside the Dialog
        // We scope the search to the 'dialog' variable found above.
        // We match by caption "Confirm" to ensure we get the right button.
        Button confirmBtn = _get(dialog, Button.class, spec -> spec.withThemes(ButtonVariant.LUMO_PRIMARY.getVariantName()));
        // 4. Click and Verify
        _click(confirmBtn);

        verify(listenerMock).onRefused(bid);
    }



}