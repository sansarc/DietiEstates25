package com.dieti.dietiestates25.services.session;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionManagerTest {

    private MockedStatic<UserSession> sessionMock;
    private UI ui;

    @BeforeEach
    void setUp() {
        MockVaadin.setup();
        ui = MockedUI.getCurrent();

        sessionMock = mockStatic(UserSession.class);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
        sessionMock.close();
    }

    @Test
    void monitorSessionSetsPollInterval() {
        sessionMock.when(UserSession::isUserLoggedIn).thenReturn(true);
        SessionManager.monitorSession(ui);

        assertEquals(120_000, ui.getPollInterval());
    }

    @Test
    void checkSessionTimeout_shouldNotShowWarning_newSession() {
        sessionMock.when(UserSession::isUserLoggedIn).thenReturn(true);
        sessionMock.when(UserSession::getSessionStart).thenReturn(System.currentTimeMillis());

        SessionManager.monitorSession(ui);
        SessionManager.checkSessionTimeout(ui);

        sessionMock.verify(UserSession::getSessionStart);
        sessionMock.verify(() -> UserSession.logout(anyBoolean()), never());
        NotificationsKt.expectNoNotifications();
    }

    @Test
    void checkSessionTimeout_shouldShowWarning_closeToExpireSession() {
        var sessionStart = System.currentTimeMillis() - (SessionManager.SESSION_TIMEOUT - SessionManager.WARNING_THRESHOLD + 1);
        sessionMock.when(UserSession::isUserLoggedIn).thenReturn(true);
        sessionMock.when(UserSession::getSessionStart).thenReturn(sessionStart);

        SessionManager.monitorSession(ui);
        SessionManager.checkSessionTimeout(ui);

        sessionMock.verify(UserSession::getSessionStart);
        sessionMock.verify(() -> UserSession.logout(anyBoolean()), never());
        NotificationsKt.expectNotifications("Your session will expire in 10 minutes.");
    }

    @Test
    void checkSessionTimeout_shouldNotShowWarning_moreThanOnce() {
        var sessionStart = System.currentTimeMillis() - (SessionManager.SESSION_TIMEOUT - SessionManager.WARNING_THRESHOLD + 1);
        sessionMock.when(UserSession::isUserLoggedIn).thenReturn(true);
        sessionMock.when(UserSession::getSessionStart).thenReturn(sessionStart);

        SessionManager.monitorSession(ui);
        SessionManager.warned = true;
        SessionManager.checkSessionTimeout(ui);
        SessionManager.warned = false;

        sessionMock.verify(UserSession::getSessionStart);
        sessionMock.verify(() -> UserSession.logout(anyBoolean()), never());
        NotificationsKt.expectNoNotifications();
    }

    @Test
    void checkSessionTimeout_shouldLogoutAndNotify_expiredSession() {
        var sessionStart = System.currentTimeMillis() - (SessionManager.SESSION_TIMEOUT + 1000);
        sessionMock.when(UserSession::isUserLoggedIn).thenReturn(true);
        sessionMock.when(UserSession::getSessionStart).thenReturn(sessionStart);

        SessionManager.monitorSession(ui);
        SessionManager.checkSessionTimeout(ui);

        sessionMock.verify(UserSession::getSessionStart);
        sessionMock.verify(() -> UserSession.logout(true));
        NotificationsKt.expectNotifications("Session expired. Redirecting...");
    }
}