package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.ad.AdInsert;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.utils.NotificationFactory;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DescriptionNMediaForm extends Form {
    static final int DESCRIPTION_CHAR_MAX = 1000;
    static final int UPLOAD_LIMIT = 20;
    int CURRENT_UPLOADS = 0;

    TextArea description;
    MultiFileMemoryBuffer buffer;
    Upload upload;
    VerticalLayout uploadLayout;
    Paragraph uploadParagraph;
    Paragraph filesFormatParagraph;
    NumberField price;
    transient List<Photo> uploadedPhotos;


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

        price = priceInEuroNumberField("Price");
        price.setWidthFull();
        price.setHelperText("Price per month if the owner is renting this property.");
        var leftLayout = new VerticalLayout(price, description);
        leftLayout.setSpacing(false);
        leftLayout.setPadding(false);


        setRequiredTrue(price);
        uploadLayout.add(uploadParagraph, upload, filesFormatParagraph);
        add(leftLayout, uploadLayout);
    }

    private void createDescription() {
        description = new TextArea("Enter a description of at least 50 characters.");
        description.setWidthFull();
        description.setMinHeight("350px");
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.setMaxLength(DESCRIPTION_CHAR_MAX);
        description.addValueChangeListener(event -> event.getSource().setHelperText(String.format("(%s/%s)", event.getValue().length(), DESCRIPTION_CHAR_MAX)));
    }

    private void createUploadComponent() {
        buffer = new MultiFileMemoryBuffer();
        uploadedPhotos = new ArrayList<>();
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(UPLOAD_LIMIT);

        upload.addSucceededListener(event -> {
            var filename = event.getFileName();

            try {
                var base64Bytes = buffer.getInputStream(filename).readAllBytes();
                var base64 = Base64.getEncoder().encodeToString(base64Bytes);
                uploadedPhotos.add(new Photo(event.getFileName(), base64));
            } catch (IOException e) {
                NotificationFactory.error("Error while uploading " + filename + ": " + e.getMessage());
            }

            CURRENT_UPLOADS++;
            if (CURRENT_UPLOADS == UPLOAD_LIMIT)
                uploadParagraph.setText("You're all set!");
            else
                uploadParagraph.setText(CURRENT_UPLOADS + " pictures uploaded " + "(max " + UPLOAD_LIMIT + ")");
        });

        upload.addFileRejectedListener(event -> NotificationFactory.error("File is too large! Max 5 MB allowed."));

        upload.addFileRemovedListener(event -> {
           if (CURRENT_UPLOADS > 0) CURRENT_UPLOADS--;
           uploadParagraph.setText(CURRENT_UPLOADS + " pictures uploaded " + "(max " + UPLOAD_LIMIT + ")");
        });
    }

    public void addFormValuesNPhotos(AdInsert ad, List<Photo> photos) {
        ad.setPrice(price.getValue());
        if (!description.getValue().isBlank())
            ad.setDescription(description.getValue());
        if (!uploadedPhotos.isEmpty())
            photos.addAll(uploadedPhotos);
    }
}
