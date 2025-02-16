package com.dieti.dietiestates25.views.notfound;

import com.dieti.dietiestates25.views.MainLayout;
import com.dieti.dietiestates25.views.ui_components.DietiEstatesLogo;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.servlet.http.HttpServletResponse;

@Route(value = "404")
public class PageNotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    DietiEstatesLogo logo = new DietiEstatesLogo("600px", "auto");
    H1 title = new H1("Oops... Page Not Found");
    Paragraph paragraph = new Paragraph("Whatever you're looking for, it is not here.");

    public PageNotFoundView() {
        configureLayout();
        configureComponents();
        add(logo, title, paragraph);
    }

    private void configureComponents() {
        paragraph.setMinHeight("300px");
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