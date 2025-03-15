    package com.dieti.dietiestates25.views.upload.forms;

    import com.dieti.dietiestates25.dto.ad.Ad;
    import com.dieti.dietiestates25.ui_components.Form;
    import com.dieti.dietiestates25.ui_components.InfoPopover;
    import com.dieti.dietiestates25.utils.EnergyClassUtils;
    import com.vaadin.flow.component.checkbox.Checkbox;
    import com.vaadin.flow.component.html.Image;
    import com.vaadin.flow.component.icon.Icon;
    import com.vaadin.flow.component.icon.VaadinIcon;
    import com.vaadin.flow.component.select.Select;
    import com.vaadin.flow.component.textfield.IntegerField;

    public class DetailsForm extends Form {

        Checkbox elevator;
        IntegerField nRooms, nBathrooms, garageSpots;
        Select<String> energyClass;

        public DetailsForm() {
            configureLayout();
            createComponents();
        }

        protected void createComponents() {
            elevator = new Checkbox("Elevator");
            nRooms = integerField("Number of rooms", new Image("/images/sofa-512.png", "sofa_icon"));
            nBathrooms = integerField("Bathrooms", new Image("/images/shower-512.png", "shower_icon"));
            garageSpots = integerField("Parking spots in a garage", new Icon(VaadinIcon.CAR));

            energyClass = select("Energy class", "A", "B", "C", "D", "E", "F", "Unknown");
            energyClass.setValue("Unknown");
            EnergyClassUtils.setRenderer(energyClass);

            nRooms.setHelperText("All rooms of the building.");
            nBathrooms.setHelperText("All kind of bathrooms.");

            new InfoPopover(garageSpots, "Parking spots within the property only. You can add more later.");
            new InfoPopover(nRooms, "You can specify the rooms division and possible usages in the description field.");
            new InfoPopover(nBathrooms, "You can provide additional info later on in the description field.");

            setRequiredTrue(nRooms, nBathrooms);

            add(
                    nRooms,
                    nBathrooms,
                    garageSpots,
                    energyClass,
                    elevator
            );
        }

        public void addFormValues(Ad ad) {
            ad.setNBathrooms(nBathrooms.getValue());
            ad.setGarageSpots(garageSpots.getValue());
            ad.setElevator(elevator.getValue());
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
