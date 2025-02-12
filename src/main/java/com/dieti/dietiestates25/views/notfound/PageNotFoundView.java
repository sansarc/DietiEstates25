package com.dieti.dietiestates25.views.notfound;

import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.servlet.http.HttpServletResponse;

@Route(value = "404")
public class PageNotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    Image logo = new Image("/images/logo.png", "logo");
    H1 title = new H1("Oops... Page Not Found");
    Paragraph paragraph = new Paragraph("Whatever you're looking for, it is not here.");

    public PageNotFoundView() {
        configureLayout();
        configureComponents();
        add(logo, title, paragraph);
    }

    private void configureComponents() {
        createLogo();
        paragraph.setMinHeight("300px");
    }

    private void createLogo() {
        logo.setWidth("600px");
        logo.setHeight("auto");
        logo.getStyle().set("margin-bottom", "70px");
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> errorParameter) {
        return HttpServletResponse.SC_NOT_FOUND;
    }

}