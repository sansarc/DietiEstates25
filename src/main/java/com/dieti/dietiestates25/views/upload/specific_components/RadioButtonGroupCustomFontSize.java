package com.dieti.dietiestates25.views.upload.specific_components;


import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class RadioButtonGroupCustomFontSize<T> extends RadioButtonGroup<T> {
    public RadioButtonGroupCustomFontSize(String label) {
        super(label);
    }

    public void resize(String fontSize) {
        getChildren()
                .forEach(field ->
                        field.getStyle().setFontSize(fontSize)
                );
    }
}
