package com.dieti.dietiestates25.observers;

import java.util.ArrayList;
import java.util.List;

public class ThemeChangeNotifier {
    private final static List<ThemeChangeListener> listeners = new ArrayList<>();

    public static void addListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    public static void notifyThemeChange(boolean darkTheme) {
        for(ThemeChangeListener listener : listeners)
            listener.onThemeChange(darkTheme);
    }
}
