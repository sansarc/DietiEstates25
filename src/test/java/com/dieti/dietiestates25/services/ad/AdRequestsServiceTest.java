package com.dieti.dietiestates25.services.ad;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dieti.dietiestates25.constants.Constants.Codes.INTERNAL_SERVER_ERROR;
import static com.dieti.dietiestates25.constants.Constants.Codes.OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdRequestsServiceTest {

    @InjectMocks
    private AdRequestsService service;

    @Mock
    private RequestService request;

    @BeforeEach
    void setUp() {
        service = new AdRequestsService();
        service.requestService = request;
    }

    @Test
    void testGetRegions_success() {
        when(request.GET(any()))
                .thenReturn(new SimpleResponse(OK, "[Region1, Region2]"));

        var result = service.getRegions();

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).GET(any());
    }

    @Test
    void testGetRegions_returnsNull() {
        when(request.GET(any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getRegions();

        assertNull(result);
        verify(request, atMostOnce()).GET(any());
    }

    @Test
    void testGetProvinces_success() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(OK, "[Province1, Province2]"));

        var result = service.getProvinces("Region1");

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetProvinces_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getProvinces("Region1");

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetCities_success() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(OK, """
                        [{"code":1, "name":"City1"}, {"code":2, "name":"City2"}]
                        """));

        var result = service.getCities("Province");

        assertNotNull(result);
        assertTrue(result.ok());
        assertEquals(2, result.getEntities().size());
        assertEquals("1", result.getEntities().getFirst().getCode());
        assertEquals("2", result.getEntities().getLast().getCode());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetCities_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getCities("Province");

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testInsertAd_success() {
        var response = new SimpleResponse(OK, """
                {
                    "entities": [{"id": 1}],
                    "message": "Operation successful."
                }
        """);

        when(request.POST(any(), any(), any(), any()))
                .thenReturn(response);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.insertAd(new Ad());

            assertNotNull(result);
            assertTrue(result.ok());
            assertEquals(1, result.getEntities().size());
            assertEquals(1, result.getEntities().getFirst().getId());
            verify(request, atMostOnce()).POST(any(), any(), any(), any());
        }
    }

    @Test
    void testInsertAd_returnsNull() {
        when(request.POST(any(), any(), any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));


        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                            .thenReturn("mock-session-id");

            var result = service.insertAd(new Ad());

            assertNull(result);
            verify(request, atMostOnce()).POST(any(), any(), any(), any());
        }
    }

    @Test
    void testUploadImages_success() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(OK, ""));

        var result = service.uploadImages(1, new Photo("file1", "base64"));

        assertNotNull(result);
        assertTrue(result.ok());
        verify(request, atMostOnce()).POST(any(), any());
    }

    @Test
    void testUploadImages_returnsNull() {
        when(request.POST(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.uploadImages(1, new Photo("file1", "base64"));

        assertNull(result);
        verify(request, atMostOnce()).POST(any(), any());
    }

    @Test
    void testGetImages_success() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(OK, """
                        [{"base64Image":"string1", "fileName":"file1"}, {"code":"string2", "fileName":"file2"}]
                        """));

        var result = service.getImages(1);

        assertNotNull(result);
        assertEquals(2, result.getEntities().size());
        assertEquals("file1", result.getEntities().getFirst().getFileName());
        assertEquals("file2", result.getEntities().getLast().getFileName());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetImages_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getImages(1);

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testSendBid_success() {
        var mockResponse = new SimpleResponse(OK, """
            {
                "entities": [
                    {
                        "ad": 10,
                        "offererMessage": "test",
                        "amount": 1.0,
                        "id": 1
                    }
                ],
                "message": "Operation successful."
            }
        """);

        when(request.POST(any(), any(), any(), any()))
                .thenReturn(mockResponse);


        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.sendBid(new Bid.Insert(1, 1.0, "test", ""));

            assertNotNull(result);
            assertTrue(result.ok());
            assertEquals(1, result.getEntities().size());
            assertEquals(10, result.getEntities().getFirst().getAdId());
            assertEquals(1, result.getEntities().getFirst().getId());
            assertEquals(1.0, result.getEntities().getFirst().getAmount());
            assertEquals("test", result.getEntities().getFirst().getOffererMessage());
            verify(request, atMostOnce()).POST(any(), any(), any(), any());
        }
    }

    @Test
    void testSendBid_returnsNull() {
        when(request.POST(any(), any(), any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.sendBid(new Bid.Insert(1, 1.0, "test", ""));

            assertNull(result);
            verify(request, atMostOnce()).POST(any(), any(), any(), any());
        }
    }

    @Test
    void testGetBids_success() {
        var mockResponse = new SimpleResponse(OK, """
            {
                "entities": [
                    {
                        "ad": 10,
                        "agentMessage": "counteroffer",
                          "counterOffer": {
                              "amount": 2.0,
                              "id": 2,
                              "parentBid": 1
                          },
                        "amount": 1.0,
                        "id": 1
                    }
                ],
                "message": "Operation successful."
            }
        """);

        when(request.GET(any(), any()))
                .thenReturn(mockResponse);

        var result = service.getBidsBy("key", "value");

        assertNotNull(result);
        assertTrue(result.ok());
        assertEquals(1, result.getEntities().size());
        assertEquals(10, result.getEntities().getFirst().getAdId());
        assertEquals(1, result.getEntities().getFirst().getId());
        assertEquals(2, result.getEntities().getFirst().getCounteroffer().getId());
        assertEquals(result.getEntities().getFirst().getCounteroffer().getParentBid(), result.getEntities().getFirst().getId());
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testGetBids_returnsNull() {
        when(request.GET(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.getBidsBy("key", "value");

        assertNull(result);
        verify(request, atMostOnce()).GET(any(), any());
    }

    @Test
    void testCancelBid_success() {
        var success = new SimpleResponse(OK, "");

        when(request.PUT(any(), any(), any(), anyMap()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.cancelBid(1);

            assertNotNull(result);
            assertTrue(result.ok());
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyMap());
        }
    }

    @Test
    void testCancelBid_returnsNull() {
        when(request.PUT(any(), any(), any(), anyMap()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.cancelBid(1);

            assertNull(result);
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyMap());
        }
    }

    @Test
    void testAcceptOrRefuseBid_success() {
        var success = new SimpleResponse(OK, "");

        when(request.PUT(any(), any(), any(), anyString()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.acceptOrRefuseBid(new Bid());

            assertNotNull(result);
            assertTrue(result.ok());
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyString());
        }
    }

    @Test
    void testAcceptOrRefuseBid_returnsNull() {
        when(request.PUT(any(), any(), any(), anyString()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.acceptOrRefuseBid(new Bid());

            assertNull(result);
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyString());
        }
    }

    @Test
    void testAcceptOrRefuseCounterOffer_success() {
        var success = new SimpleResponse(OK, "");

        when(request.PUT(any(), any(), any(), anyString()))
                .thenReturn(success);

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.acceptOrRefuseCounterOffer(new Bid());

            assertNotNull(result);
            assertTrue(result.ok());
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyString());
        }
    }

    @Test
    void testAcceptOrRefuseCounterOffer_returnsNull() {
        when(request.PUT(any(), any(), any(), anyString()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        try (var sessionMock = mockStatic(UserSession.class)) {
            sessionMock.when(UserSession::getSessionId)
                    .thenReturn("mock-session-id");

            var result = service.acceptOrRefuseCounterOffer(new Bid());

            assertNull(result);
            verify(request, atMostOnce()).PUT(any(), any(), any(), anyString());
        }
    }

    @Test
    void testSearchAds_success() {
        var response = new SimpleResponse(OK, """
                {
                    "entities": [{"id": 1}],
                    "message": "Operation successful."
                }
        """);

        when(request.PUT(any(), any()))
                .thenReturn(response);

        var result = service.searchAds(new Ad.SearchBy());

        assertNotNull(result);
        assertTrue(result.ok());
        assertEquals(1, result.getEntities().size());
        assertEquals(1, result.getEntities().getFirst().getId());
        verify(request, atMostOnce()).PUT(any(), any());
    }

    @Test
    void testSearchAds_returnsNull() {
        when(request.PUT(any(), any()))
                .thenReturn(new SimpleResponse(INTERNAL_SERVER_ERROR, ""));

        var result = service.searchAds(new Ad.SearchBy());

        assertNull(result);
        verify(request, atMostOnce()).PUT(any(), any());
    }
}