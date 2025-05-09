package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.ad.Photo;
import com.vaadin.componentfactory.addons.splide.ImageSlide;
import com.vaadin.componentfactory.addons.splide.Splide;

import java.util.List;

public class ImagesCarousel extends Splide {

    public ImagesCarousel(List<Photo> photos) {
        super();
        setWidth("800px");
        setHeight("500px");
        getStyle().setMarginBottom("50px");

        if (photos == null || photos.isEmpty()) return;

        for (var photo : photos) {
            var image = photo.toImage();
            image.setSizeFull();
            var slide = new ImageSlide(image.getSrc());
            addSlide(slide);
        }
    }
}
