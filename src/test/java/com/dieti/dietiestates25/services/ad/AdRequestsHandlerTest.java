package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.dto.EntityResponse;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.City;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.views.ad.AdView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.dieti.dietiestates25.constants.Constants.Codes.OK;
import static com.dieti.dietiestates25.utils.TestUtils.mockResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdRequestsHandlerTest {

    @Mock
    private UI ui;
    
    @Captor
    private ArgumentCaptor<Class<? extends Component>> navigationCaptor;

    @Mock
    private AdRequestsService service;

    @InjectMocks
    private AdRequestsHandler handler;

    @BeforeEach
    void setUp() {
        UI.setCurrent(ui);

        handler = new AdRequestsHandler();
        handler.adRequestsService = service;
    }

    private void verifyNavigationWithParamsTo(Class<? extends Component> target) {
        verify(ui, atMostOnce()).navigate(navigationCaptor.capture(), (RouteParameters) any());
        assertEquals(target, navigationCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    private void verifyNoNavigation() {
        verify(ui, never()).navigate((Class<? extends Component>) any());
    }

    @Test
    void testGetRegions_success() {
        var mockResponse = new SimpleResponse(OK, "[\"Region1\", \"Region2\"]");
        when(service.getRegions()).thenReturn(mockResponse);

        var regions = handler.getRegions();

        assertNotNull(regions);
        assertEquals(2, regions.size());
        assertEquals("Region1", regions.get(0));
        assertEquals("Region2", regions.get(1));
        verify(service, atMostOnce()).getRegions();
    }

    @Test
    void testGetRegions_nullResponse() {
        when(service.getRegions()).thenReturn(null);

        var regions = handler.getRegions();

        assertNotNull(regions);
        assertTrue(regions.contains("No regions found."));
        verify(service, atMostOnce()).getRegions();
    }

    @Test
    void testGetProvinces_success() {
        when(service.getProvinces(any()))
                .thenReturn(new SimpleResponse(OK, "[\"Province1\", \"Province2\"]"));

        var provinces = handler.getProvinces("Region");

        assertNotNull(provinces);
        assertEquals(2, provinces.size());
        assertEquals("Province1", provinces.get(0));
        assertEquals("Province2", provinces.get(1));
        verify(service, atMostOnce()).getProvinces(any());
    }

    @Test
    void testGetProvinces_emptyResponse() {
        when(service.getProvinces(any()))
                .thenReturn(new SimpleResponse(OK, "[]"));

        var result = handler.getProvinces("Region");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("No provinces found.", result.getFirst());
        verify(service, atMostOnce()).getProvinces(any());
    }

    @Test
    void testGetProvinces_nullResponse() {
        when(service.getProvinces(any()))
                .thenReturn(null);

        var provinces = handler.getProvinces("Region");

        assertNotNull(provinces);
        assertTrue(provinces.contains("No provinces found."));
        verify(service, atMostOnce()).getProvinces(any());
    }

    @Test
    void testGetCities_success() {
        var mockResponse = new EntityResponse<City>();
        mockResponse.setEntities(List.of(new City("city1", "1"), new City("city2", "2")));
        when(service.getCities(any()))
                .thenReturn(mockResponse);

        var cities = handler.getCities("Province");

        assertNotNull(cities);
        assertEquals(2, cities.size());
        assertEquals("1", cities.getFirst().getCode());
        assertEquals("2", cities.getLast().getCode());
        verify(service, atMostOnce()).getCities(any());
    }

    @Test
    void testGetCities_emptyResponse() {
        var mockResponse = new EntityResponse<City>();
        mockResponse.setEntities(List.of());
        when(service.getCities(any()))
                .thenReturn(mockResponse);

        var result = handler.getCities("Province");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("No cities found.", result.getFirst().getName());
        verify(service, atMostOnce()).getCities(any());
    }

    @Test
    void testGetCities_nullResponse() {
        when(service.getCities(any()))
                .thenReturn(null);

        var result = handler.getCities("Province");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("No cities found.", result.getFirst().getName());
        verify(service, atMostOnce()).getCities(any());
    }

    @Test
    void testInsertAd_success() {
        var ad = new AdInsert();
        var photos = List.of(new Photo("file1", "base64"), new Photo("file2", "base64"));

        var mockPhotoResponse = mockResponse(true);
        var ad1 = new Ad();
        ad1.setId(1);
        EntityResponse<Ad> mockAdResponse = new EntityResponse<>();
        mockAdResponse.setStatusCode(OK);
        mockAdResponse.setEntities(List.of(ad1));

        when(service.insertAd(ad)).thenReturn(mockAdResponse);
        when(service.uploadImages(anyInt(), any())).thenReturn(mockPhotoResponse);

        handler.insertAd(ad, photos);

        verify(service, atMostOnce()).insertAd(ad);
        verify(service, times(2)).uploadImages(anyInt(), any());
        verifyNavigationWithParamsTo(AdView.class);
    }

    @Test
    void testInsertAd_photoFailure() {
        var ad = new AdInsert();
        var photos = List.of(new Photo("file1", "base64"), new Photo("file2", "base64"));

        var mockPhotoResponse = mockResponse(false);
        var ad1 = new Ad();
        ad1.setId(1);
        EntityResponse<Ad> mockAdResponse = new EntityResponse<>();
        mockAdResponse.setStatusCode(OK);
        mockAdResponse.setEntities(List.of(ad1));

        when(service.insertAd(ad)).thenReturn(mockAdResponse);
        when(service.uploadImages(anyInt(), any())).thenReturn(mockPhotoResponse);

        handler.insertAd(ad, photos);

        verify(service, atMostOnce()).insertAd(ad);
        verify(service, times(2)).uploadImages(anyInt(), any());
        verifyNavigationWithParamsTo(AdView.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testInsertAd_failure() {
        var ad = new AdInsert();
        EntityResponse<Ad> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        when(mockResponse.getMessage()).thenReturn("");
        when(service.insertAd(ad)).thenReturn(mockResponse);

        handler.insertAd(ad, List.of());

        verify(service, atMostOnce()).insertAd(ad);
        verify(service, never()).uploadImages(anyInt(), any());
        verifyNoMoreInteractions(service);
        verifyNoNavigation();
    }

    @Test
    void testInsertAd_nullResponse() {
        var ad = new AdInsert();
        List<Photo> photos = List.of();
        when(service.insertAd(ad)).thenReturn(null);

        handler.insertAd(ad, photos);

        verify(service, atMostOnce()).insertAd(ad);
        verify(service, never()).uploadImages(anyInt(), any());
        verifyNoMoreInteractions(service);
        verifyNoNavigation();
    }

    @Test
    void testGetAd_success() {
        var ad = new Ad();
        ad.setId(1);
        ad.setAC(true);

        EntityResponse<Ad> mockResponse = new EntityResponse<>();
        mockResponse.setStatusCode(OK);
        mockResponse.setEntities(List.of(ad));
        when(service.searchAds(any())).thenReturn(mockResponse);

        var photoResponse = new EntityResponse<Photo>();
        photoResponse.setStatusCode(OK);
        photoResponse.setEntities(List.of(new Photo("file1", "base64")));
        when(service.getImages(anyInt())).thenReturn(photoResponse);

        var result = handler.getAd(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertTrue(result.isAC());
        assertNotNull(result.getPhotos());
        assertEquals(1, result.getPhotos().size());
        assertEquals("file1", result.getPhotos().getFirst().getFileName());

        verify(service, atMostOnce()).searchAds(any());
        verify(service, atMostOnce()).getImages(anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAd_failedAdSearch() {
        EntityResponse<Ad> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.searchAds(any())).thenReturn(mockResponse);

        var resultAd = handler.getAd(1);

        assertNull(resultAd);
        verify(service, atMostOnce()).searchAds(any());
        verify(service, never()).getImages(anyInt());
        verifyNoMoreInteractions(service);
    }

    @Test
    void testGetAd_nullResponse() {
        when(service.searchAds(any())).thenReturn(null);

        var resultAd = handler.getAd(1);

        assertNull(resultAd);
        verify(service, atMostOnce()).searchAds(any());
        verify(service, never()).getImages(anyInt());
        verifyNoMoreInteractions(service);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testRetrievePhotos_failure() {       // other cases were covered by other tests
        EntityResponse<Photo> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.getImages(anyInt())).thenReturn(mockResponse);

        handler.retrievePhotos(new Ad());

        verify(service, atMostOnce()).getImages(anyInt());
    }

    @Test
    void testSendBid_success() {
        var bid = new Bid();
        bid.setId(1);
        bid.setAmount(1.0);

        var mockResponse = new EntityResponse<Bid>();
        mockResponse.setStatusCode(OK);
        mockResponse.setEntities(List.of(bid));

        when(service.sendBid(any())).thenReturn(mockResponse);

        var result = handler.sendBid(1, 200.0, "Test bid message");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1.0, result.getAmount());
        verify(service, atMostOnce()).sendBid(any(Bid.Insert.class));
    }

    @Test
    void testSendBid_nullResponse() {
        when(service.sendBid(any(Bid.Insert.class))).thenReturn(null);

        var result = handler.sendBid(1, 150.0, "Test bid message");

        assertNull(result);
        verify(service, atMostOnce()).sendBid(any(Bid.Insert.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSendBid_failure() {
        EntityResponse<Bid> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.sendBid(any(Bid.Insert.class))).thenReturn(mockResponse);

        var result = handler.sendBid(1, 300.0, "Another test bid message");

        assertNull(result);
        verify(service, atMostOnce()).sendBid(any(Bid.Insert.class));
    }

    @Test
    void testGetBidsBy_success() {
        var bid1 = new Bid();
        bid1.setId(1);
        bid1.setAmount(1.0);
        var bid2 = new Bid();
        bid2.setId(2);
        bid2.setAmount(2.0);

        var mockResponse = new EntityResponse<Bid>();
        mockResponse.setStatusCode(OK);
        mockResponse.setEntities(List.of(bid1, bid2));

        when(service.getBidsBy(any(), any())).thenReturn(mockResponse);

        var result = handler.getBidsBy("key", "value");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals(2, result.getLast().getId());
        verify(service, atMostOnce()).getBidsBy(any(), any());
    }

    @Test
    void testGetBidsBy_nullResponse() {
        when(service.getBidsBy(any(), any())).thenReturn(null);

        var result = handler.getBidsBy("key", "value");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).getBidsBy(any(), any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetBidsBy_failure() {
        EntityResponse<Bid> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.getBidsBy(any(), any())).thenReturn(mockResponse);

        var result = handler.getBidsBy("key", "value");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).getBidsBy(any(), any());
    }

    @Test
    void testCancelBid_success() {
        when(service.cancelBid(anyInt())).thenReturn(new SimpleResponse(OK, ""));
        handler.cancelBid(1);
        verify(service, atMostOnce()).cancelBid(anyInt());
    }

    @Test
    void testCancelBid_failure() {
        var failure = mockResponse(false);
        when(service.cancelBid(anyInt())).thenReturn(failure);
        handler.cancelBid(1);
        verify(service, atMostOnce()).cancelBid(anyInt());
    }

    @Test
    void testCancelBid_nullResponse() {
        when(service.cancelBid(anyInt())).thenReturn(null);
        handler.cancelBid(1);
        verify(service, atMostOnce()).cancelBid(anyInt());
    }

    @Test
    void testAcceptOrRefuseBid_success() {
        when(service.acceptOrRefuseBid(any())).thenReturn(new SimpleResponse(OK, ""));

        var result = handler.acceptOrRefuseBid(new Bid.Counteroffer());

        assertTrue(result);
        verify(service, atMostOnce()).acceptOrRefuseBid(any());
    }

    @Test
    void testAcceptOrRefuseBid_failure() {
        var failure = mockResponse(false);
        when(service.acceptOrRefuseBid(any())).thenReturn(failure);

        var result = handler.acceptOrRefuseBid(new Bid.Counteroffer());

        assertFalse(result);
        verify(service, atMostOnce()).acceptOrRefuseBid(any());
    }

    @Test
    void testAcceptOrRefuseBid_nullResponse() {
        when(service.acceptOrRefuseBid(any())).thenReturn(null);

        var result = handler.acceptOrRefuseBid(new Bid.Counteroffer());

        assertFalse(result);
        verify(service, atMostOnce()).acceptOrRefuseBid(any());
    }

    @Test
    void testAcceptCounterOffer_success_accepted() {
        when(service.acceptOrRefuseCounterOffer(any()))
                .thenReturn(new SimpleResponse(OK, ""));

        var result = handler.acceptOrRefuseCounterOffer(new Bid.Accept(1));

        assertTrue(result);
        verify(service, atMostOnce()).acceptOrRefuseCounterOffer(any());
    }

    @Test
    void testRefuseCounterOffer_success_refused() {
        when(service.acceptOrRefuseCounterOffer(any()))
                .thenReturn(new SimpleResponse(OK, ""));

        var result = handler.acceptOrRefuseCounterOffer(new Bid.Counteroffer.Refuse(1));

        assertTrue(result);
        verify(service, atMostOnce()).acceptOrRefuseCounterOffer(any());
    }

    @Test
    void testAcceptOrRefuseCounterOffer_failure() {
        var failureResponse = mockResponse(false);
        when(service.acceptOrRefuseCounterOffer(any(Bid.Counteroffer.class)))
                .thenReturn(failureResponse);

        var result = handler.acceptOrRefuseCounterOffer(new Bid.Counteroffer());

        assertFalse(result);
        verify(service, atMostOnce()).acceptOrRefuseCounterOffer(any());
    }

    @Test
    void testAcceptOrRefuseCounterOffer_nullResponse() {
        when(service.acceptOrRefuseCounterOffer(any()))
                .thenReturn(null);

        var result = handler.acceptOrRefuseCounterOffer(new Bid.Counteroffer());

        assertFalse(result);
        verify(service, atMostOnce()).acceptOrRefuseCounterOffer(any());
    }

    @Test
    void testGetAdsByAgent_success() {
        // sell ad
        var sellAd = new Ad();
        sellAd.setId(1);
        sellAd.setType("S");

        // rent ad
        var rentAd = new Ad();
        rentAd.setId(2);
        rentAd.setType("R");

        // response for sells
        var sellResponse = new EntityResponse<Ad>();
        sellResponse.setStatusCode(OK);
        sellResponse.setEntities(List.of(sellAd));

        // response for rents
        var rentResponse = new EntityResponse<Ad>();
        rentResponse.setStatusCode(OK);
        rentResponse.setEntities(List.of(rentAd));

        when(service.searchAds(argThat(search -> search != null && "S".equals(search.getType()))))
                .thenReturn(sellResponse);
        when(service.searchAds(argThat(search -> search != null && "R".equals(search.getType()))))
                .thenReturn(rentResponse);

        var result = handler.getAdsByAgent("agent1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals("S", result.getFirst().getType());
        assertEquals(2, result.getLast().getId());
        assertEquals("R", result.getLast().getType());

        verify(service, times(2)).searchAds(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAdsByAgent_failure() {
        EntityResponse<Ad> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.searchAds(any())).thenReturn(mockResponse);

        var result = handler.getAdsByAgent("agent1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).searchAds(any());
    }

    @Test
    void getAdsByAgent_nullResponse() {
        when(service.searchAds(any())).thenReturn(null);

        var result = handler.getAdsByAgent("agent1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).searchAds(any());
    }

    @Test
    void testSearchAds_success() {
        var ad1 = new Ad();
        var ad2 = new Ad();
        ad1.setId(1);
        ad2.setId(2);

        var mockResponse = new EntityResponse<Ad>();
        mockResponse.setStatusCode(OK);
        mockResponse.setEntities(List.of(ad1, ad2));

        when(service.searchAds(any())).thenReturn(mockResponse);

        var result = handler.searchAds(new Ad.SearchBy());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals(2, result.getLast().getId());
        verify(service, atMostOnce()).searchAds(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSearchAds_failure() {
        EntityResponse<Ad> mockResponse = mock(EntityResponse.class);
        when(mockResponse.ok()).thenReturn(false);
        mockResponse.setEntities(List.of());

        when(service.searchAds(any())).thenReturn(mockResponse);

        var result = handler.searchAds(new Ad.SearchBy());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).searchAds(any());
    }

    @Test
    void testSearchAds_nullResponse() {
        when(service.searchAds(any())).thenReturn(null);

        var result = handler.searchAds(new Ad.SearchBy());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, atMostOnce()).searchAds(any());
    }

    @Test
    void testBadAdResponse_success() {
        var success = mockResponse(true);
        var result = handler.badAdResponse(success);
        assertFalse(result);
    }

    @Test
    void testBadAdResponse_failure() {
        var failure = mockResponse(false);
        var result = handler.badAdResponse(failure);
        assertTrue(result);
    }

    @Test
    void testBadAdResponse_nullResponse() {
        var result = handler.badAdResponse(null);
        assertTrue(result);
    }
}