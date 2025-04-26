package com.dieti.dietiestates25.dto.ad;

import com.vaadin.flow.component.html.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Photo {
    String fileName;
    String base64Image;

    public Photo(String fileName, String base64Image) {
        this.fileName = fileName;
        this.base64Image = base64Image;
    }

    public Image toImage() {
        var mimeType = guessMimeType(fileName);
        var base64DataUrl = "data:" + mimeType + ";base64," + base64Image;
        return new Image(base64DataUrl, fileName);
    }

    private String guessMimeType(String fileName) {
        if (fileName == null) return "image/jpeg";
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".png")) return "image/png";
        return "image/jpeg";
    }
}
