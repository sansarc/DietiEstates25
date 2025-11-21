package com.dieti.dietiestates25.utils;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class DeleteButton extends Button {
    public DeleteButton(ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(LineAwesomeIcon.TRASH_ALT.create());
        addThemeVariants(ButtonVariant.LUMO_ERROR);
        setTooltipText("Delete");
        getStyle()
                .setCursor("pointer")
                .setMarginTop("40px")
                .setMarginRight("30px");

        var dialog = new ConfirmDialog();
        dialog.setText("Are you sure you want to delete this item?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setCancelText("Cancel");
        dialog.setConfirmButtonTheme("error primary");

        addClickListener(e -> dialog.open());

        dialog.addCancelListener(e -> dialog.close());
        dialog.addConfirmListener(e -> {
            clickListener.onComponentEvent(new ClickEvent<>(this));
            dialog.close();
        });
    }
}
