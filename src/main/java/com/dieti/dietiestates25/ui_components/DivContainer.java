package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.observers.ThemeChangeListener;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class DivContainer extends Div implements ThemeChangeListener {

    public DivContainer(String width, String height) {
        ThemeChangeNotifier.addListener(this);
        addDetachListener(event -> ThemeChangeNotifier.removeListener(this));
        Boolean darkTheme = (Boolean) VaadinSession.getCurrent().getAttribute("darkTheme");
        applyTheme(darkTheme != null ? darkTheme : false);

        configureLayout(width, height);
    }

    private void configureLayout(String width, String height) {
        addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE,
                LumoUtility.Gap.XSMALL,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN
        );

        setWidth(width);
        setHeight(height);
    }

    @Override
    public void onThemeChange(boolean darkTheme) {
        applyTheme(darkTheme);
    }

    private void applyTheme(boolean darkTheme) {
        if (darkTheme)
            getStyle().setBackgroundColor(Constants.Colors.GRAY_OVER_DARKMODE);
    }
}
