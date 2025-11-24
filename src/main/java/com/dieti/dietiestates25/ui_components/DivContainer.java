package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.observers.ThemeChangeListener;
import com.dieti.dietiestates25.observers.ThemeChangeNotifier;
import com.dieti.dietiestates25.services.session.UserSession;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class DivContainer extends Div implements ThemeChangeListener {

    public DivContainer(String width, String height) {
        ThemeChangeNotifier.addListener(this);
        addDetachListener(event -> ThemeChangeNotifier.removeListener(this));
        Boolean darkTheme = UserSession.isDarkThemeOn();
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
        var bg = darkTheme ? Constants.Colors.GRAY_OVER_DARKMODE : Constants.Colors.SECONDARY_GRAY;
        getStyle().setBackgroundColor(bg);
    }
}
