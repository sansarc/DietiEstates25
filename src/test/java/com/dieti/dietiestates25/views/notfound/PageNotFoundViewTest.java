package com.dieti.dietiestates25.views.notfound;

import com.dieti.dietiestates25.ui_components.DietiEstatesLogo;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class PageNotFoundViewTest {
    private PageNotFoundView view;

    @Mock
    private BeforeEnterEvent beforeEnterMock;

    @Mock
    private ErrorParameter<NotFoundException> errorParameterMock;

    @BeforeEach
    void setUp() {
        view = new PageNotFoundView();
    }

    @Test
    void keyComponentsShouldBeInitialized() {
        assertEquals(3, view.getComponentCount());
        assertInstanceOf(DietiEstatesLogo.class, view.getComponentAt(0));
        assertInstanceOf(H1.class, view.getComponentAt(1));
        assertInstanceOf(Paragraph.class, view.getComponentAt(2));
    }

    @Test
    void testSetErrorParameter_withoutCustomMessage() {
        when(errorParameterMock.hasCustomMessage()).thenReturn(false);

        int result = view.setErrorParameter(beforeEnterMock, errorParameterMock);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, result);
    }

    @Test
    void testSetErrorParameter_withCustomMessage() {
        when(errorParameterMock.hasCustomMessage())
                .thenReturn(true);
        when(errorParameterMock.getCaughtException())
                .thenReturn(new NotFoundException("Custom message"));

        int result = view.setErrorParameter(beforeEnterMock, errorParameterMock);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, result);
    }
}