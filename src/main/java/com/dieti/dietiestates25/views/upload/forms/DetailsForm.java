    package com.dieti.dietiestates25.views.upload.forms;

    import com.dieti.dietiestates25.dto.ad.AdInsert;
    import com.dieti.dietiestates25.ui_components.Form;
    import com.dieti.dietiestates25.ui_components.InfoPopover;
    import com.dieti.dietiestates25.utils.EnergyClassUtils;
    import com.vaadin.flow.component.checkbox.Checkbox;
    import com.vaadin.flow.component.checkbox.CheckboxGroup;
    import com.vaadin.flow.component.html.Image;
    import com.vaadin.flow.component.select.Select;
    import com.vaadin.flow.component.textfield.IntegerField;

    import java.util.Set;

    public class DetailsForm extends Form {

        Checkbox elevator;
        IntegerField nRooms, nBathrooms;
        Select<String> energyClass;
        CheckboxGroup<String> otherServices;
        Checkbox AC;

        public DetailsForm() {
            configureLayout();
            createComponents();
        }

        protected void createComponents() {
            elevator = new Checkbox("Elevator");
            nRooms = integerField("Number of rooms", new Image("/images/sofa-512.png", "sofa_icon"));
            nBathrooms = integerField("Bathrooms", new Image("/images/shower-512.png", "shower_icon"));

            energyClass = select("Energy class", "Unknown", "A", "B", "C", "D", "E", "F");
            energyClass.setValue("Unknown");
            EnergyClassUtils.setRenderer(energyClass);

            AC = new Checkbox("AC");

            otherServices = new CheckboxGroup<>();
            otherServices.setLabel("Other services");
            otherServices.setItems("Private garage", "Condominium parking", "Doorman service");


            nRooms.setHelperText("All rooms of the building.");
            nBathrooms.setHelperText("All kind of bathrooms.");

            new InfoPopover(nRooms, "You can specify the rooms division and possible usages in the description field.");
            new InfoPopover(nBathrooms, "You can provide additional info later on in the description field.");

            setRequiredTrue(nRooms, nBathrooms);

            add(
                    nRooms,
                    nBathrooms,
                    energyClass,
                    elevator,
                    AC,
                    otherServices
            );
        }

        public void addFormValues(AdInsert ad) {
            ad.setNRooms(nRooms.getValue());
            ad.setNBathrooms(nBathrooms.getValue());
            ad.setElevator(elevator.getValue());

            if (!energyClass.getValue().equals("Unknown"))
                ad.setEnergyClass(energyClass.getValue());

            ad.setAC(AC.getValue());

            Set<String> selectedServices = otherServices.getSelectedItems();
            ad.setPrivateGarage(selectedServices.contains("Private garage"));
            ad.setCondominiumParking(selectedServices.contains("Condominium parking"));
            ad.setDoormanService(selectedServices.contains("Doorman service"));
        }

        protected void configureLayout() {
            setWidth("70%");
            setResponsiveSteps(
                    new ResponsiveStep("0", 1),
                    new ResponsiveStep("500px", 3)
            );
        }
    }
