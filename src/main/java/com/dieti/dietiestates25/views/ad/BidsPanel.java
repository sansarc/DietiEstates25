package com.dieti.dietiestates25.views.ad;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.observers.BidActionListener;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.ui_components.BidMessage;
import com.dieti.dietiestates25.ui_components.DivContainer;
import com.dieti.dietiestates25.ui_components.Form;
import com.dieti.dietiestates25.views.profile.Profile;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

public class BidsPanel extends DivContainer implements BidActionListener {

    private final AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    @Getter private static VerticalLayout bidsListLayout;
    private Scroller scroller = new Scroller();

    @Setter
    @Getter
    private static boolean isOneAccepted;

    public BidsPanel(Ad ad) {
        super("28%", "auto");
        setMaxHeight("700px");
        getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setRight("0")
                .setTop("0")
                .setTransition("transform 0.1s")
                .setTransform("translateX(-2%)");

        bidsListLayout = new VerticalLayout();
        isOneAccepted = false;

        createAgentSection(ad);
        add(new Hr());
        createBidForm(ad);
        add(new Hr());
        add(new H4("Bids for this ad"));
        createBidsList(ad);
    }

    private void createBidsList(Ad ad) {
        bidsListLayout.removeAll();

        var bids = adRequestsHandler.getBidsBy("ad", ad.getId());

        for (var bid : bids) {
            if (!bid.getStatus().equals("C")) {
                var bidMessage = new BidMessage(bid, ad.getAgent().getEmail(), this);

                if (bid.getStatus().equals("A")) {
                    isOneAccepted = true;   // if there's an accepted bid
                    bidMessage.addComponentAsFirst(new H5("Accepted bid"));
                    if (bid.getCounteroffer() == null)
                        bidMessage.setAccepted();
                    bidsListLayout.addComponentAsFirst(bidMessage);
                }
                else if (bid.getStatus().equals("R")) {
                    bidMessage.setRefused();
                    bidsListLayout.add(bidMessage);
                }
                else {
                    if (isOneAccepted) bidMessage.disableButtons();  // then all the others will be disabled no matter status
                    bidsListLayout.add(bidMessage);
                }
            }
        }

        if (bidsListLayout.getComponentCount() > 0) {
            scroller.setContent(bidsListLayout);
            add(scroller);
        }
        else
            add(new Span("No bids found for this ad."));
    }

    @Override
    public void onAccepted(Bid bid) {
        bidsListLayout.getChildren()
                .forEach(component -> {
                    if (component instanceof BidMessage bidMessage) {
                        bidMessage.disableButtons();
                        
                        if (bidMessage.getBid().getId() == bid.getId()) {
                            bidsListLayout.remove(bidMessage);
                            bidsListLayout.addComponentAsFirst(bidMessage);
                            bidMessage.setAccepted();
                        }
                    }
                });
    }

    @Override
    public void onRefused(Bid bid) {
        var refusedBidMessage = find(bid.getId());
        refusedBidMessage.setRefused();
    }

    @Override
    public void onDeleted(Bid bid) {
        bidsListLayout.remove(find(bid.getId()));
    }

    public BidMessage find(int id) {
        AtomicReference<BidMessage> bidMessage = new AtomicReference<>();

        bidsListLayout.getChildren()
                .forEach(component -> {
                    if (component instanceof BidMessage i) {
                        if (i.getBid().getId() == id)
                            bidMessage.set(i);
                    }
                });

        return bidMessage.get();
    }

    private void createBidForm(Ad ad) {
        var title = new H4("Make an offer for this listing");
        title.getStyle().setMarginBottom("10px");

        var priceField = Form.priceInEuroNumberField("");
        var messageField = new TextField("Optional Message");
        messageField.setPlaceholder("Explain the reason of your offer...");
        messageField.getStyle().setMarginBottom("12px");

        var sendBtn = new Button("Send", e -> {
            if (priceField.isEmpty() || priceField.getValue() <= 0) {
                priceField.setErrorMessage("Please enter a valid value.");
                priceField.setInvalid(true);
                return;
            }

            var bid = adRequestsHandler.sendBid(ad.getId(), priceField.getValue(), messageField.getValue());
            if (bid != null) {
                if (bidsListLayout.getComponentCount() > 0 && bidsListLayout.getComponentAt(0) instanceof Span)  // basically if it's the first being added, remove the "no bids found" thing
                    bidsListLayout.removeAll();

                bid.setFirstname(UserSession.getFirstName()); bid.setLastname(UserSession.getLastName());
                bidsListLayout.add(new BidMessage(bid, ad.getAgent().getEmail(), this));
                bidsListLayout.setVisible(false); bidsListLayout.setVisible(true); // forcing refresh
                scroller.scrollToBottom();
            }
        });

        sendBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendBtn.addClickShortcut(Key.ENTER);
        if (isOneAccepted) sendBtn.setEnabled(false);

        add(title, priceField, messageField, sendBtn);
    }

    private void createAgentSection(Ad ad) {
        var agentTitle = new H3("About the agent");

        var agentName = ad.getAgent().getFirstName() + " " + ad.getAgent().getLastName();
        var avatar = new Avatar(agentName);
        avatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        avatar.getStyle().set("border-color", Constants.Colors.PRIMARY_BLUE).setCursor("pointer");

        var agentLink = new RouterLink(agentName, Profile.class, new RouteParameters("email", ad.getAgent().getEmail()));
        agentLink.getElement().addEventListener("click", event -> Profile.cacheUser(ad.getAgent()));
        agentLink.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var layout = new HorizontalLayout(avatar, agentLink);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().setMarginTop("20px").setCursor("pointer");

        add(agentTitle, layout);
    }

}
