package com.dieti.dietiestates25.services.logging;

import com.vaadin.flow.server.VaadinServlet;
import jakarta.servlet.ServletException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppServlet extends VaadinServlet {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getGlobal().setLevel(Level.FINEST);
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
    }
}
