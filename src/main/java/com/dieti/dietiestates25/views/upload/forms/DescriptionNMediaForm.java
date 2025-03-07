package com.dieti.dietiestates25.views.upload.forms;

import com.dieti.dietiestates25.ui_components.Form;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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

    public DescriptionNMediaForm() {
        configureLayout();
        createComponents();
        addComponents();
    }

    protected void configureLayout() {
        setWidth("80%");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
    }

    protected void createComponents() {
        description = getTextArea();
        buffer = new MultiFileMemoryBuffer();
        upload = getUploadComponent();
        uploadParagraph = getUploadParagraph();
        filesFormatParagraph = getFilesFormatParagraph();
        uploadLayout = getUploadLayout();
    }

    private VerticalLayout getUploadLayout() {
        var uploadLayout = new VerticalLayout(uploadParagraph, filesFormatParagraph, upload);
        uploadLayout.setSpacing(false);
        uploadLayout.setPadding(false);
        uploadLayout.setMargin(false);
        uploadLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        uploadLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return uploadLayout;
    }

    private static Paragraph getUploadParagraph() {
        var paragraph = new Paragraph(CURRENT_UPLOADS + " pictures over " + UPLOAD_LIMIT + " uploaded.");
        paragraph.getStyle().set("margin-bottom", "var(--lumo-space-xxs)");

        return paragraph;
    }

    private static Paragraph getFilesFormatParagraph() {
        var paragraph = new Paragraph("(Accepted file formats: JPG, PNG, GIF)");
        paragraph.getStyle()
                .set("margin-top", "var(--lumo-space-xxs)")
                .set("font-size", "12px")
                .set("color", "var(--lumo-secondary-text-color)");

        return paragraph;
    }

    private static TextArea getTextArea() {
        var textArea = new TextArea("Enter a description of at least 50 characters.");
        textArea.setSizeFull();
        textArea.setMinHeight("350px");
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.setRequired(true);

        textArea.addValueChangeListener(event -> {
            int charsToType = DESCRIPTION_TEXTAREA_CHAR_LIMIT - event.getValue().length();
            if (charsToType > 0)
                event.getSource().setHelperText(charsToType + " more characters still to type.");
            else
                event.getSource().setHelperText("You can type more, but that's already okay!");
        });

        return textArea;
    }

    private Upload getUploadComponent() {
        var upload = new Upload(buffer);
        upload.setMaxFiles(20);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(UPLOAD_LIMIT);
        CURRENT_UPLOADS = 0;

        upload.addSucceededListener(event -> {
            String filename = event.getFileName();
            try (InputStream inputStream = buffer.getInputStream(filename)) {
                // Process the input stream here
            } catch (IOException e) {
                // Handle exception
            }

            CURRENT_UPLOADS++;
            if (CURRENT_UPLOADS == UPLOAD_LIMIT)
                uploadParagraph.setText("You're all set!");
            else
                uploadParagraph.setText(CURRENT_UPLOADS + " pictures over " + UPLOAD_LIMIT + " uploaded.");
        });

        return upload;
    }

    protected void addComponents() {
        add(description, uploadLayout);
    }

}
