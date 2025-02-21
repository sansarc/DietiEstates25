package com.dieti.dietiestates25.views.upload.specific_components;

import com.dieti.dietiestates25.utils.FormFieldFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OtherCostsEntryForm extends VerticalLayout {
    private final List<CostEntryRow> costEntries = new ArrayList<>();
    private final VerticalLayout entriesContainer = new VerticalLayout();

    public OtherCostsEntryForm() {
        entriesContainer.setSpacing(true);
        entriesContainer.setPadding(false);
        entriesContainer.setWidth("100%");

        addNewCostEntry();

        Button addButton = new Button("Add Another Cost", VaadinIcon.PLUS.create());
        addButton.addClickListener(e -> addNewCostEntry());
        addButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        add(entriesContainer, addButton);
        setPadding(false);
        setSpacing(true);
    }

    private void addNewCostEntry() {
        CostEntryRow entryRow = new CostEntryRow();
        costEntries.add(entryRow);
        entriesContainer.add(entryRow);
    }

    private class CostEntryRow extends HorizontalLayout {
        private final TextArea descriptionField;
        private final NumberField priceField;
        private final Button removeButton;

        public CostEntryRow() {
            descriptionField = new TextArea("Additional cost", "Description");
            descriptionField.setWidth("75%");

            priceField = FormFieldFactory.createPriceInEuroNumberField("", false);
            priceField.setPlaceholder("Price");
            priceField.setWidth("25%");

            removeButton = new Button(VaadinIcon.TRASH.create());
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
            removeButton.addClickListener(e -> {
                costEntries.remove(this);
                entriesContainer.remove(this);
            });

            setAlignItems(Alignment.BASELINE);
            add(descriptionField, priceField, removeButton);
            setPadding(false);
            setSpacing(true);
            setWidth("100%");
        }

        public String getDescription() {
            return descriptionField.getValue();
        }

        public Double getPrice() {
            return priceField.getValue();
        }

        public void setDescription(String description) {
            descriptionField.setValue(description);
        }

        public void setPrice(Double price) {
            priceField.setValue(price);
        }
    }

    public List<Map<String, Object>> getCostEntries() {
        return costEntries.stream()
                .map(row -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("description", row.getDescription());
                    entry.put("price", row.getPrice());
                    return entry;
                })
                .collect(Collectors.toList());
    }

}