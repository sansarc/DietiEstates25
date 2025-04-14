package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.services.session.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.select.Select;

public class ThemeManager {

    public static void initializeTheme(Select<String> themeSelect) {
        if (UserSession.isDarkThemeOn() == null) {
            setTheme(false);
            themeSelect.setValue("light");
        } else {
            boolean isDark = UserSession.isDarkThemeOn();
            setTheme(isDark);
            themeSelect.setValue(isDark ? "dark" : "light");
        }
    }

    public static void applyThemeChange(String value, Select<String> themeSelect) {
        boolean dark = "dark".equals(value);
        UserSession.setTheme(dark);
        setTheme(dark);
        themeSelect.setValue(value);
    }

    public static void setTheme(boolean dark) {
        UI.getCurrent().getElement().executeJs(
                "document.documentElement.setAttribute('theme', $0)",
                dark ? "dark" : "light"
        );
    }

    public static boolean isDarkThemeOn() {
        Boolean dark = UserSession.isDarkThemeOn();
        return dark != null && dark;
    }
}
