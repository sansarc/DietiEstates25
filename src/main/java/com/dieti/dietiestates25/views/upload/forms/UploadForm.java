package com.dieti.dietiestates25.views.upload.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UploadForm extends FormLayout {

    protected UploadForm() {
        configureBase();
    }

    private void configureBase() {
        getStyle().set("margin-bottom", "var(--lumo-space-l)");

        getElement().executeJs(
                "this.querySelectorAll('*').forEach(el => {" +
                        "  if(el.shadowRoot) {" +
                        "    el.shadowRoot.querySelector('[part=\"label\"]')?.style.setProperty('color', 'black');" +
                        "  }" +
                        "});"
                );
    }

    protected abstract void configureLayout();
    protected abstract void createComponents();
    protected abstract void addComponents();

    protected final void setRequiredIndicatorVisibleTrue(HasValue...components) {
        for (HasValue component : components)
            component.setRequiredIndicatorVisible(true);
    }

    public boolean areRequiredFieldsValid() {
        AtomicBoolean fieldsValidation = new AtomicBoolean(true);

        this.getChildren()
                .filter(child -> child instanceof HasValue<?,?>)
                .map(child -> (HasValue<?,?>)child)
                .forEach(hasValue -> {
                    if (hasValue.isRequiredIndicatorVisible() && hasValue.getValue() == null)
                        fieldsValidation.set(false);
                });

        return fieldsValidation.get();
    }

}
