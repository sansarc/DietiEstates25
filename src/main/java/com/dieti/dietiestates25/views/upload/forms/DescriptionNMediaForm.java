package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.io.IOException;
import java.io.InputStream;

public class DescriptionNMediaForm extends Form {
    public static final int DESCRIPTION_TEXTAREA_CHAR_LIMIT = 50;
    public static final int UPLOAD_LIMIT = 20;
    public static int CURRENT_UPLOADS = 0;

    TextArea description;
    MultiFileMemoryBuffer buffer;
    Upload upload;
    VerticalLayout uploadLayout;
    Paragraph uploadParagraph;
    Paragraph filesFormatParagraph;

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

        var price = priceInEuroNumberField("Price", true);
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
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(UPLOAD_LIMIT);
        CURRENT_UPLOADS = 0;

        upload.addSucceededListener(event -> {
            String filename = event.getFileName();
            try (InputStream inputStream = buffer.getInputStream(filename)) {
            } catch (IOException e) {
            }

            CURRENT_UPLOADS++;
            if (CURRENT_UPLOADS == UPLOAD_LIMIT)
                uploadParagraph.setText("You're all set!");
            else
                uploadParagraph.setText(CURRENT_UPLOADS + " pictures uploaded " + "(max " + UPLOAD_LIMIT + ")");
        });
    }

}
