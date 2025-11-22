package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.ad.Photo;
import com.vaadin.componentfactory.addons.splide.ImageSlide;
import com.vaadin.componentfactory.addons.splide.Splide;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;

import java.util.List;

// Requesting the Splide NPM package and CSS for production (in development it's loaded dynamically)
@NpmPackage(value = "@splidejs/splide", version = "3.6.12")
@CssImport("@splidejs/splide/dist/css/splide.min.css")
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
