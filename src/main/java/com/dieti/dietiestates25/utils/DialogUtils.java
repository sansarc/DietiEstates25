package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;

public class DialogUtils {

    public static void closeOpenDialogs() {
        UI.getCurrent().getChildren()
                .filter(component -> component instanceof Dialog)
                .map(component -> (Dialog) component)
                .findFirst()
                .ifPresent(dialog -> UI.getCurrent().access(dialog::close));
    }

}
