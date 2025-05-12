    package com.dieti.dietiestates25.views.upload.forms;

    import com.dieti.dietiestates25.dto.ad.AdInsert;
    import com.dieti.dietiestates25.ui_components.Form;
    import com.dieti.dietiestates25.ui_components.InfoPopover;
    import com.dieti.dietiestates25.utils.EnergyClassUtils;
    import com.vaadin.flow.component.checkbox.Checkbox;
    import com.vaadin.flow.component.checkbox.CheckboxGroup;
    import com.vaadin.flow.component.select.Select;
    import com.vaadin.flow.component.textfield.IntegerField;
    import org.vaadin.lineawesome.LineAwesomeIcon;

    import java.util.Set;

    public class DetailsForm extends Form {

        public static final String UNKNOWN = "Unknown";
        Checkbox elevator;
        IntegerField nRooms;
        IntegerField nBathrooms;
        Select<String> energyClass;
        CheckboxGroup<String> otherServices;
        Checkbox airConditioning;

        public DetailsForm() {
            configureLayout();
            createComponents();
        }

        protected void createComponents() {
            elevator = new Checkbox("Elevator");
            nRooms = integerField("Number of rooms", LineAwesomeIcon.COUCH_SOLID.create());
            nBathrooms = integerField("Bathrooms", LineAwesomeIcon.BATH_SOLID.create());
            energyClass = select("Energy class", UNKNOWN, "A", "B", "C", "D", "E", "F");
            energyClass.setValue(UNKNOWN);
            EnergyClassUtils.setRenderer(energyClass);
            airConditioning = new Checkbox("AC");
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
                    airConditioning,
                    otherServices
            );
        }

        public void addFormValues(AdInsert ad) {
            ad.setNRooms(nRooms.getValue());
            ad.setNBathrooms(nBathrooms.getValue());
            ad.setElevator(elevator.getValue());

            if (!energyClass.getValue().equals(UNKNOWN))
                ad.setEnergyClass(energyClass.getValue());

            ad.setAC(airConditioning.getValue());

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
