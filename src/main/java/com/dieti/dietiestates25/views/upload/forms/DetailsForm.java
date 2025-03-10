    package com.dieti.dietiestates25.views.upload.forms;

    import com.dieti.dietiestates25.dto.ad.AdRequest;
    import com.dieti.dietiestates25.ui_components.Form;
    import com.dieti.dietiestates25.ui_components.InfoPopover;
    import com.dieti.dietiestates25.utils.EnergyClassUtils;
    import com.vaadin.flow.component.checkbox.Checkbox;
    import com.vaadin.flow.component.html.Image;
    import com.vaadin.flow.component.icon.Icon;
    import com.vaadin.flow.component.icon.VaadinIcon;
    import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
    import com.vaadin.flow.component.select.Select;
    import com.vaadin.flow.component.textfield.IntegerField;

    public class DetailsForm extends Form {

        Checkbox elevator, disabledAmenities;
        IntegerField bedrooms, roomsGeneral, nBathrooms, garageSpots;
        Select<String> energyClass;

        public DetailsForm() {
            configureLayout();
            createComponents();
        }

        protected void createComponents() {
            elevator = new Checkbox("Elevator");
            bedrooms = integerField("Bedrooms", new Icon(VaadinIcon.BED));
            roomsGeneral = integerField("Other rooms", new Image("/images/sofa-512.png", "sofa_icon"));
            nBathrooms = integerField("Bathrooms", new Image("/images/shower-512.png", "shower_icon"));
            garageSpots = integerField("Parking spots in a garage", new Icon(VaadinIcon.CAR));
            disabledAmenities = new Checkbox("Disabled amenities");

            energyClass = new Select<>();
            energyClass.setLabel("Energy class");
            energyClass.setItems("A", "B", "C", "D", "E", "F");
            EnergyClassUtils.setRenderer(energyClass);

            var parkingInfo = new InfoPopover(garageSpots, "In this category are considered ONLY the parking spots within the property. You can add more inx`    the description field.");

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

        public void addFormValues(AdRequest ad) {
            ad.setnBedrooms(bedrooms.getValue());
            ad.setnRoomsGeneral(roomsGeneral.getValue());
            ad.setNBathrooms(nBathrooms.getValue());
            ad.setGarageSpots(garageSpots.getValue());
            ad.setElevator(elevator.getValue());
            ad.setDisabledAmenities(disabledAmenities.getValue());
            ad.setEnergyClass(energyClass.getValue().charAt(0));
        }

        protected void configureLayout() {
            setWidth("70%");
            setResponsiveSteps(
                    new ResponsiveStep("0", 1),
                    new ResponsiveStep("500px", 3)
            );
        }
    }
