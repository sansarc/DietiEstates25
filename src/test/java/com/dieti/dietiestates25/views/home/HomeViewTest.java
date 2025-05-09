package com.dieti.dietiestates25.views.home;

import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.views.search.SearchView;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HomeViewTest {

    private HomeView view;

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

        view = new HomeView();
        view.adRequestsHandler = handlerMock;
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void searchButtonShouldDoNothingWhenSearchTextFielIsEmpty() {
        assertTrue(view.searchText.isEmpty());

        view.searchButton.click();

        assertInstanceOf(HomeView.class, MockedUI.getCurrent().getCurrentView());
    }

    @Test
    void searchButtonShouldNavigateToSearchView() {
        view.searchText.setValue("Somewhere");
        view.searchButton.click();

        assertInstanceOf(SearchView.class, MockedUI.getCurrent().getCurrentView());
    }
}
