package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.ad.AdRequest;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescriptionNMediaForm extends Form {
    public final int DESCRIPTION_TEXTAREA_CHAR_LIMIT = 50;
    public final int UPLOAD_LIMIT = 20;
    public int CURRENT_UPLOADS = 0;

    TextArea description;
    MultiFileMemoryBuffer buffer;
    Upload upload;
    VerticalLayout uploadLayout;
    Paragraph uploadParagraph, filesFormatParagraph;
    NumberField price;
    List<Photo> uploadedPhotos;


    public  DescriptionNMediaForm() {
        configureLayout();
        createComponents();
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        uploadLayout = new VerticalLayout();
        uploadLayout.setSpacing(false);
        uploadLayout.setPadding(false);
        uploadLayout.setMargin(false);
        uploadLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        uploadLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    protected void createComponents() {
        createDescription();

        uploadParagraph = new Paragraph(CURRENT_UPLOADS + " pictures uploaded " + "(max " + UPLOAD_LIMIT + ")");
        uploadParagraph.getStyle().setMarginBottom("var(--lumo-space-xxs)");

        filesFormatParagraph = new Paragraph("(Accepted file formats: JPG, PNG, GIF)");
        filesFormatParagraph.getStyle()
                .setMarginTop("var(--lumo-space-xxs)")
                .setFontSize("12px")
                .setColor(Constants.Colors.SECONDARY_GRAY);

        createUploadComponent();

        price = priceInEuroNumberField("Price", true);
        price.setWidthFull();
        price.setHelperText("Type the full price without dots or commas, we'll take care of that.");
        var leftLayout = new VerticalLayout(price, description);
        leftLayout.setSpacing(false);
        leftLayout.setPadding(false);

        uploadLayout.add(uploadParagraph, upload, filesFormatParagraph);
        add(leftLayout, uploadLayout);
    }

    private void createDescription() {
        description = new TextArea("Enter a description of at least 50 characters.");
        description.setWidthFull();
        description.setMinHeight("350px");
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.setRequired(true);

        description.addValueChangeListener(event -> {
            int charsToType = DESCRIPTION_TEXTAREA_CHAR_LIMIT - event.getValue().length();
            if (charsToType > 0)
                event.getSource().setHelperText(charsToType + " more characters still to type.");
            else
                event.getSource().setHelperText("You can type more, but that's already okay!");
        });
    }

    private void createUploadComponent() {
        buffer = new MultiFileMemoryBuffer();
        uploadedPhotos = new ArrayList<>();
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(UPLOAD_LIMIT);

        upload.addSucceededListener(event -> {
            String filename = event.getFileName();

            try {
                uploadedPhotos.add(new Photo(filename, buffer.getInputStream(filename).readAllBytes()));
            } catch (IOException e) {
                Notification.show("Error while uploading " + filename + ": " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                throw new RuntimeException(e);
            }

            CURRENT_UPLOADS++;
            if (CURRENT_UPLOADS == UPLOAD_LIMIT)
                uploadParagraph.setText("You're all set!");
            else
                uploadParagraph.setText(CURRENT_UPLOADS + " pictures uploaded " + "(max " + UPLOAD_LIMIT + ")");
        });
    }

    public void addValues(AdRequest ad) {
        ad.setPrice(price.getValue());
        ad.setDescription(description.getValue());
        ad.setPhotos(uploadedPhotos);
    }

}
