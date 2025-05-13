package com.dieti.dietiestates25.views.search;

import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.ui_components.AdCard;
import com.dieti.dietiestates25.utils.TestUtils;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchViewTest {

    private SearchView view;

    @Mock
    private AdRequestsHandler handlerMock;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();

        view = new SearchView();
        view.adRequestsHandler = handlerMock;
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testBuildQueryParams_allFieldsFilled() {
        view.type.setValue(SearchView.FOR_SALE);
        view.nRooms.setValue(3);
        view.nBathrooms.setValue(2);
        view.minPrice.setValue(100000.0);
        view.maxPrice.setValue(300000.0);
        view.locationComponents.region.setValue("Campania");
        view.locationComponents.province.setValue("NA");
        view.locationComponents.city.setItems(new City("Napoli", "123"));
        view.locationComponents.city.setValue("123");
        view.locationComponents.address.setValue("Via Roma 1");

        String query = view.buildQueryParams(
                view.type.getValue(),
                view.nRooms.getValue(),
                view.nBathrooms.getValue(),
                view.locationComponents,
                view.minPrice.getValue(),
                view.maxPrice.getValue()
        );

        assertTrue(query.contains("?q=sale"));
        assertTrue(query.contains("&nrooms=3"));
        assertTrue(query.contains("&nbathrooms=2"));
        assertTrue(query.contains("&region=Campania"));
        assertTrue(query.contains("&province=NA"));
        assertTrue(query.contains("&city=123"));
        assertTrue(query.contains("&address=Via Roma 1"));
        assertTrue(query.contains("&min=100000.0"));
        assertTrue(query.contains("&max=300000.0"));
    }

    @Test
    void testBuildQueryParams_MinimalFields() {
        view.type.setValue(SearchView.FOR_RENT);

        String query = view.buildQueryParams(
                view.type.getValue(),
                null,
                null,
                view.locationComponents,
                null,
                null
        );

        assertEquals("?q=rent", query);
    }

    @Test
    void testRefresh_withAds() {
        var ad = TestUtils.mockAd();
        view.refresh(List.of(ad));

        assertEquals(1, view.sortedAds.size());
        view.adsFilters.removeFromParent(); // removing filters layout from adsList to make the assertion below possible
        assertEquals(view.sortedAds.size(), view.adsList.getComponentCount());
        assertEquals(2, view.agencyFilter.getListDataView().getItemCount());
        assertEquals(ad.getAgent().getAgencyName(), view.agencyFilter.getListDataView().getItem(1)); // item at 0 is "All"
        assertTrue(view.adsFoundCount.getText().contains("1"));
    }

    @Test
    void refresh_withNoAds() {
        view.refresh(List.of());

        assertNull(view.sortedAds);
        assertEquals(1, view.adsList.getComponentCount()); // filters will be there whatsoever
        assertEquals("No ads found.", view.adsFoundCount.getText());
        assertTrue(view.agencyFilter.isEmpty());

        view.refresh(List.of(TestUtils.mockAd()));
        view.refresh(List.of());

        assertNotNull(view.sortedAds);
        assertEquals(1, view.sortedAds.size());
        assertEquals(1, view.adsList.getComponentCount()); // filters will be there whatsoever
        assertEquals("No ads found.", view.adsFoundCount.getText());
        assertTrue(view.agencyFilter.isEmpty());
    }

    @Test
    void testDisplay_filterByAgency() {
        var ad1 = TestUtils.mockAd();
        var ad2 = TestUtils.mockAd();
        ad2.getAgent().setAgencyName("otherAgency");
        
        view.refresh(List.of(ad1, ad2));
        view.agencyFilter.setValue("otherAgency");

        assertEquals(List.of("All", ad1.getAgent().getAgencyName(), ad2.getAgent().getAgencyName()),
                view.agencyFilter.getListDataView().getItems().toList());

        assertEquals(1, view.adsList.getComponentCount() - 1);
        assertEquals(ad2, ((AdCard) view.adsList.getComponentAt(1)).getAd());

        view.agencyFilter.setValue(ad1.getAgent().getAgencyName());
        assertEquals(1, view.adsList.getComponentCount() - 1);
        assertEquals(ad1, ((AdCard) view.adsList.getComponentAt(1)).getAd());

        view.agencyFilter.setValue("All");
        assertEquals(2, view.adsList.getComponentCount() - 1);
        assertEquals(ad1, ((AdCard) view.adsList.getComponentAt(1)).getAd());
    }

    @Test
    void testHandleSearchClick_validForm() {
        view.nRooms.setValue(2);

        when(handlerMock.searchAds(any()))
                .thenReturn(TestUtils.mockAdsList());

        view.search.click();

        assertTrue(view.adsFoundCount.getText().contains("2"));
        assertEquals(2, view.sortedAds.size());
        assertEquals(3, view.adsList.getComponentCount());
    }

    @Test
    void testHandleSearchClick_invalidForm() {
        view.form.clear();
        view.search.click();

        NotificationsKt.expectNotifications("Fill at least one field to make a search.");
        verifyNoInteractions(handlerMock);
    }
}