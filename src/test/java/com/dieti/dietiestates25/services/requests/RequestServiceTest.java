package com.dieti.dietiestates25.services.requests;

import com.dieti.dietiestates25.constants.Constants.Codes;
import com.dieti.dietiestates25.utils.NotificationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    private static final String TEST_ENDPOINT = "http://test-api.com/resource";
    private static final String TEST_JSON_PAYLOAD = "{\"key\":\"value\"}";
    private static final String TEST_RESPONSE = "{\"result\":\"success\"}";

    private RequestService requestService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() throws Exception {
        requestService = new RequestService();

        var restTemplateField = RequestService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        var restTemplate = (RestTemplate) restTemplateField.get(requestService);

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testPOST_success() {
        // server expectations
        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.POST(TEST_ENDPOINT, TEST_JSON_PAYLOAD);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPOST_withParams_success() {
        var params = new HashMap<String, String>();
        params.put("param1", "value1");
        params.put("param2", "value2");

        var expectedUrl = TEST_ENDPOINT + "?param1=value1&param2=value2";

        mockServer.expect(ExpectedCount.once(), requestTo(expectedUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.POST(TEST_ENDPOINT, params, TEST_JSON_PAYLOAD);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPOST_withHeader_success() {
        var headerName = "Authorization";
        var headerValue = "Bearer token123";

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header(headerName, headerValue))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.POST(TEST_ENDPOINT, headerName, headerValue, TEST_JSON_PAYLOAD);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPOST_clientError() {
        var errorResponse = "{\"error\":\"Bad Request\"}";

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withBadRequest().body(errorResponse).contentType(MediaType.APPLICATION_JSON));

        var result = requestService.POST(TEST_ENDPOINT, TEST_JSON_PAYLOAD);

        assertEquals(400, result.getStatusCode());
        assertEquals(errorResponse, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPOST_runtimeException() {
        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andRespond(request -> { throw new RuntimeException("Network error"); });

        try (MockedStatic<NotificationFactory> notificationFactoryMock = Mockito.mockStatic(NotificationFactory.class)) {
            var result = requestService.POST(TEST_ENDPOINT, TEST_JSON_PAYLOAD);

            assertEquals(Codes.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertEquals("", result.getRawBody());

            notificationFactoryMock.verify(() -> NotificationFactory.criticalError(anyString()));
        }
    }

    @Test
    void testGET_success() {
        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.GET(TEST_ENDPOINT);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testGET_withParams_success() {
        var params = new HashMap<String, Serializable>();
        params.put("param1", "value1");
        params.put("param2", 123);

        var expectedUrl = TEST_ENDPOINT + "?param1=value1&param2=123";

        mockServer.expect(ExpectedCount.once(), requestTo(expectedUrl))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.GET(TEST_ENDPOINT, params);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testGET_clientError() {
        var errorResponse = "{\"error\":\"Not Found\"}";

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body(errorResponse).contentType(MediaType.APPLICATION_JSON));

        var result = requestService.GET(TEST_ENDPOINT);

        assertEquals(404, result.getStatusCode());
        assertEquals(errorResponse, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPUT_success() {
        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.PUT(TEST_ENDPOINT, TEST_JSON_PAYLOAD);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPUT_withHeaderNParams_success() {
        var headerName = "Authorization";
        var headerValue = "Bearer token123";

        var params = new HashMap<String, Serializable>();
        params.put("param1", "value1");
        params.put("param2", 123);

        var expectedUrl = TEST_ENDPOINT + "?param1=value1&param2=123";

        mockServer.expect(ExpectedCount.once(), requestTo(expectedUrl))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(headerName, headerValue))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.PUT(TEST_ENDPOINT, headerName, headerValue, params);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPUT_withHeaderNPayload_success() {
        var headerName = "Authorization";
        var headerValue = "Bearer token123";

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(headerName, headerValue))
                .andExpect(content().string(TEST_JSON_PAYLOAD))
                .andRespond(withSuccess(TEST_RESPONSE, MediaType.APPLICATION_JSON));

        var result = requestService.PUT(TEST_ENDPOINT, headerName, headerValue, TEST_JSON_PAYLOAD);

        assertEquals(200, result.getStatusCode());
        assertEquals(TEST_RESPONSE, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testPUT_clientError() {
        var errorResponse = "{\"error\":\"Unauthorized\"}";

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED).body(errorResponse).contentType(MediaType.APPLICATION_JSON));

        var result = requestService.PUT(TEST_ENDPOINT, TEST_JSON_PAYLOAD);

        assertEquals(401, result.getStatusCode());
        assertEquals(errorResponse, result.getRawBody());

        mockServer.verify();
    }

    @Test
    void testResponseLogging() {
        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        requestService.GET(TEST_ENDPOINT);

        mockServer.reset();

        mockServer.expect(ExpectedCount.once(), requestTo(TEST_ENDPOINT))
                .andRespond(withSuccess("test data ".repeat(1000), MediaType.APPLICATION_JSON));

        requestService.GET(TEST_ENDPOINT);
    }
}