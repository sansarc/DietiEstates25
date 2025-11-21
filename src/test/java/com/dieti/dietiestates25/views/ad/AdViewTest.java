package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.ImagesCarousel;
import com.dieti.dietiestates25.ui_components.InteractiveMap;
import com.dieti.dietiestates25.utils.TestUtils;
import com.dieti.dietiestates25.views.home.HomeView;
import com.dieti.dietiestates25.views.notfound.PageNotFoundView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdViewTest {

    private AdView view;

    @Mock
    private BeforeEnterEvent beforeEnterMock;

    @Mock
    private AdRequestsHandler handlerMock;

    private static Routes routes;

    @BeforeAll
    static void setupRoutes() {
        routes = new Routes().autoDiscoverViews("com.dieti.dietiestates25.views");
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);

        view = new AdView();
        view.adRequestsHandler = handlerMock;
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testKeyComponents_fromAdRequest() {
        var ad = TestUtils.mockAd();

        when(handlerMock.getAd(ad.getId()))
                .thenReturn(ad);

        stubNTrigger_beforeEnterEvent();

        // main components
        assertTrue(view.getChildren().anyMatch(ImagesCarousel.class::isInstance));
        assertTrue(view.getChildren().anyMatch(HorizontalLayout.class::isInstance));

        assertNotNull(view.scroller);
        assertNotNull(view.layout);
        assertNotNull(view.scrollerContent);

        // descriptionDiv & mapDiv
        assertTrue(view.scrollerContent.getChildren().allMatch(DivContainer.class::isInstance));

        var descriptionDiv = view.scrollerContent.getComponentAt(0);
        assertTrue(descriptionDiv.getChildren().anyMatch(H3.class::isInstance));
        assertTrue(descriptionDiv.getChildren().anyMatch(HorizontalLayout.class::isInstance));
        assertTrue(descriptionDiv.getChildren().anyMatch(Span.class::isInstance)); // descriptionText
        assertTrue(descriptionDiv.getChildren().anyMatch(HorizontalLayout.class::isInstance)); // badges
    }

    @Test
    void testKeyComponents_adFromCache() {
        AdView.cacheAd(TestUtils.mockAd());

        stubNTrigger_beforeEnterEvent();

        verifyNoInteractions(handlerMock);

        // main components
        assertTrue(view.getChildren().anyMatch(ImagesCarousel.class::isInstance));
        assertTrue(view.getChildren().anyMatch(HorizontalLayout.class::isInstance));

        assertNotNull(view.scroller);
        assertNotNull(view.layout);
        assertNotNull(view.scrollerContent);

        // descriptionDiv & mapDiv
        assertTrue(view.scrollerContent.getChildren().allMatch(DivContainer.class::isInstance));

        var descriptionDiv = view.scrollerContent.getComponentAt(0);
        assertTrue(descriptionDiv.getChildren().anyMatch(H3.class::isInstance));
        assertTrue(descriptionDiv.getChildren().anyMatch(HorizontalLayout.class::isInstance));
        assertTrue(descriptionDiv.getChildren().anyMatch(Span.class::isInstance)); // descriptionText
        assertTrue(descriptionDiv.getChildren().anyMatch(HorizontalLayout.class::isInstance)); // badges

        var mapDiv = view.scrollerContent.getComponentAt(1);
        assertTrue(mapDiv.getChildren().anyMatch(InteractiveMap.class::isInstance)); // map
    }

    @Test
    void testAdNotFoundShouldForwardToHomeView() {
        when(handlerMock.getAd(999))
                .thenReturn(null);

        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("id", "999"));

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock).forwardTo(HomeView.class);
    }

    @Test
    void testInvalidParamShouldForwardToPageNotFoundView() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters());

        view.beforeEnter(beforeEnterMock);

        verify(beforeEnterMock).forwardTo(PageNotFoundView.class);
        verifyNoInteractions(handlerMock);
    }

    @Test
    void testScrollerSideComponents_withCompleteAd() {
        var ad = TestUtils.mockAd();
        ad.setAC(true);
        ad.setPrivateGarage(true);
        ad.setCondominiumParking(true);
        ad.setDoormanService(true);
        ad.setSchool350m(true);
        ad.setLeisurePark350m(true);
        ad.setPublicTransport350m(true);

        view.createScrollerSide(ad);

        countBadges(0, 1);
        countBadges(1, 3);
    }

    @Test
    void testScrollerSideComponents_withMinimalAd() {
        var ad = TestUtils.mockAd();
        ad.setDescription("");
        ad.setCoordinates(null);

        view.createScrollerSide(ad);

        assertEquals("No description given.", view.descriptionText.getText());

        countBadges(0, 1);
        countBadges(1, 0);
    }

    private void countBadges(int componentAtInScrollerContent, int expectedCount) {
        view.scrollerContent.getComponentAt(componentAtInScrollerContent)
                .getChildren()
                .filter(HorizontalLayout.class::isInstance)
                .map(HorizontalLayout.class::cast)
                .findAny()
                .ifPresent(badges -> assertEquals(expectedCount, badges.getComponentCount()));
    }

    private void stubNTrigger_beforeEnterEvent() {
        when(beforeEnterMock.getRouteParameters())
                .thenReturn(new RouteParameters("id", "1"));

        view.beforeEnter(beforeEnterMock);
    }
}