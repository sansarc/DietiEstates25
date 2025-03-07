    package com.dieti.dietiestates25.views.upload.forms;

    import com.dieti.dietiestates25.ui_components.Form;
    import com.dieti.dietiestates25.ui_components.InfoPopover;
    import com.dieti.dietiestates25.utils.EnergyClassUtils;
    import com.vaadin.flow.component.checkbox.Checkbox;
    import com.vaadin.flow.component.combobox.ComboBox;
    import com.vaadin.flow.component.html.Image;
    import com.vaadin.flow.component.html.Span;
    import com.vaadin.flow.component.icon.Icon;
    import com.vaadin.flow.component.icon.VaadinIcon;
    import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
    import com.vaadin.flow.component.orderedlayout.VerticalLayout;
    import com.vaadin.flow.component.select.Select;
    import com.vaadin.flow.data.renderer.ComponentRenderer;
    import com.vaadin.flow.dom.Style;
    import lombok.Getter;

    public class DetailsForm extends Form {

        public DetailsForm() {
            configureLayout();
            createComponents();
        }

        protected void createComponents() {
            var elevator = new Checkbox("Elevator");
            var bedrooms = integerField("Bedrooms", new Icon(VaadinIcon.BED));
            var roomsGeneral = integerField("Other rooms", new Image("/images/sofa-512.png", "sofa_icon"));
            var nBathrooms = integerField("Bathrooms", new Image("/images/shower-512.png", "shower_icon"));
            var garageSpots = integerField("Parking spots in a garage", new Icon(VaadinIcon.CAR));
            var parkingInfo = new InfoPopover(garageSpots, "In this category are considered ONLY the parking spots within the property. You can add more inx`    the description field.");
            var disabledAmenities = new Checkbox("Disabled amenities");

            var energyClass = new Select<String>();
            energyClass.setLabel("Energy class");
            energyClass.setItems("A", "B", "C", "D", "E", "F");
            EnergyClassUtils.setRenderer(energyClass);

            var checkBoxes = new HorizontalLayout(elevator, disabledAmenities);
            checkBoxes.setPadding(false);
            checkBoxes.setSpacing(false);

            add(
                    bedrooms,
                    roomsGeneral,
                    nBathrooms,
                    garageSpots,
                    energyClass,
                    checkBoxes,
                    parkingInfo
            );
        }

        protected void configureLayout() {
            setWidth("70%");
            setResponsiveSteps(
                    new ResponsiveStep("0", 1),
                    new ResponsiveStep("500px", 3)
            );
        }
    }
