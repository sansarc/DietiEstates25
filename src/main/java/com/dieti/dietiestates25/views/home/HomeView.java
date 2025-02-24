package com.dieti.dietiestates25.views.home;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.ui_components.DivCard;
import com.dieti.dietiestates25.ui_components.DivCardsHorizontalSlider;
import com.dieti.dietiestates25.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;


@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    Div titleContainer;
    TextField searchText;
    Button searchButton;
    HorizontalLayout searchLayout;
    Div background;
    VerticalLayout backgroundContent;
    VerticalLayout secondTitleContainer;
    DivCardsHorizontalSlider cardSlider;

    public HomeView() {
        configureLayout();
        configureComponents();
    }

    private void configureComponents() {
        createTitle();
        createSearchLayout();
        createBackground();

        var cards = new ArrayList<DivCard>();
        for (int i = 0; i < 6; i++)
            cards.add(new DivCard(
                    "https://picsum.photos/2670/1780",
                    400000,
                    Constants.StaticPaths.favicon,
                    "DietiEstates",
                    "2 baths | 3 bedrooms"
            ));
        cardSlider = new DivCardsHorizontalSlider(cards);

        createSecondTitle();

        background.add(backgroundContent);
        add(background, new Hr(), secondTitleContainer, cardSlider);
    }

    private void createSecondTitle() {
        var secondTitle = new H1("Last Listings");
        var subtitle = new Paragraph("Check out the latest uploaded homes in the last days.");

        secondTitleContainer = new VerticalLayout(secondTitle, subtitle);
        secondTitleContainer.setWidth(cardSlider.getCardsWrapperActualWidth());
        secondTitleContainer.setSpacing(false);
        secondTitleContainer.setPadding(false);
        secondTitleContainer.getStyle().setMarginBottom("-30px");
    }

    private void createTitle() {
        var base = new H1("Where");
        var slidingDownPart = new H1("Agents");
        var slidingInPart = new H1("meet Clients.");

        base.getStyle()
                .setColor("white")
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px");

        slidingDownPart.getStyle()
                .setColor("white")
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px")
                .setPosition(Style.Position.RELATIVE)
                .set("animation", "slideDown 2s ease-out");


        slidingInPart.getStyle()
                .setColor("white")
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setPadding("5px")
                .setPosition(Style.Position.RELATIVE)
                .set("animation", "slideIn 3.5s ease-out");

        getElement().appendChild(getAnimations());
        titleContainer = new Div(base, slidingDownPart, slidingInPart);
    }

    private Element getAnimations() {
        Element style = new Element("style");
        style.setText("""
                @keyframes slideIn {
                    0% {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    100% {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
                @keyframes slideDown {
                    0% {
                        transform: translateY(-100%);
                        opacity: 0;
                    }
                    100% {
                        transform: translateY(0);
                        opacity: 1;
                    }
                }
        """);
        return style;
    }

    private void createBackground() {
        background = new Div();
        background.setSizeFull();
        background.setHeight("500px");
        background.getStyle()
                .setPosition(Style.Position.RELATIVE)
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .set("background-image", "url(images/background.jpg)")
                .set("background-size", "cover")
                .set("background-repeat", "no-repeat")
                .set("background-position", "center");

        wrapBackgroundContent();
    }

    private void wrapBackgroundContent() {
        backgroundContent = new VerticalLayout(titleContainer, searchLayout);
        backgroundContent.setSizeFull();
        backgroundContent.getStyle()
                .setTransform("translateY(60px)")
                .setMarginLeft("30px");

    }

    private void createSearchLayout() {
        createSearchText();
        createSearchButton();
        searchLayout = new HorizontalLayout(searchText, searchButton);
        searchLayout.setSpacing(false);
        searchLayout.setWidthFull();
        searchLayout.setPadding(false);
        searchLayout.getStyle()
                .setMarginLeft("10px")
                .setTransform("translateY(25px)");

        searchLayout.setAlignSelf(Alignment.CENTER, searchButton);
    }

    private void createSearchButton() {
        searchButton = new Button();
        searchButton.setHeight("50px");
        searchButton.setIcon(VaadinIcon.SEARCH.create());
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickShortcut(Key.ENTER);
    }

    private void createSearchText() {
        searchText = new TextField("", "Enter a City.");
        searchText.setWidth("35%");
        searchText.getStyle()
                .setBackgroundColor("transparent")
                .set("--vaadin-input-field-placeholder-color", "white")
                .set("--vaadin-input-field-height", "50px")
                .set("--vaadin-input-field-value-color", "white")
                .set("--vaadin-input-field-value-font-size", "24px")
                .set("--vaadin-input-field-value-font-weight", LumoUtility.FontWeight.BOLD);
    }

    private void configureLayout() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setPadding(false);
    }

}
