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
import com.dieti.dietiestates25.views.profile.ProfileView;
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

import java.util.List;

public class BidsPanel extends DivContainer implements BidActionListener {

    transient AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    @Getter private VerticalLayout bidsListLayout;
    private final Scroller scroller = new Scroller();

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

    private boolean allBidsAreCanceled(List<Bid> bids) {
        for (var bid : bids)
            if (!bid.getStatus().equals("C")) return false;
        return true;
    }

     public void createBidsList(Ad ad) {
        bidsListLayout.removeAll();

        var bids = adRequestsHandler.getBidsBy("AD", ad.getId());

        if (bids.isEmpty() || allBidsAreCanceled(bids))
            bidsListLayout.add(new Span("No bids found for this ad."));
        else {
            for (var bid : bids)
                createNDefineBidMessage(ad, bid);
        }

        scroller.setContent(bidsListLayout);

        if (scroller.getParent().isEmpty())
            add(scroller);
    }

    private void createNDefineBidMessage(Ad ad, Bid bid) {
        if (!bid.getStatus().equals("C")) {
            var bidMessage = new BidMessage(bid, ad.getAgent().getEmail(), this);

            if (bid.getStatus().equals("A")) {
                isOneAccepted = true;   // if there's an accepted bid
                var accepted = new H5("Accepted bid");
                accepted.getStyle().setColor(Constants.Colors.PRIMARY_BLUE);
                bidMessage.addComponentAsFirst(accepted);
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
    public void onAdded(Bid bid, String agentEmail) {
        bidsListLayout.addComponentAsFirst(new BidMessage(bid, agentEmail, this));
    }

    @Override
    public void onRefused(Bid bid) {
        var refusedBidMessage = BidMessage.find(bidsListLayout, bid.getId());
        refusedBidMessage.setRefused();
    }

    @Override
    public void onDeleted(Bid bid) {
        bidsListLayout.remove(BidMessage.find(bidsListLayout, bid.getId()));
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

                bid.setFirstname(UserSession.getFirstName());
                bid.setLastname(UserSession.getLastName());

                onAdded(bid, ad.getAgent().getEmail());
                scroller.scrollToBottom();

                priceField.clear();
                messageField.clear();
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

        var agentLink = new RouterLink(agentName, ProfileView.class, new RouteParameters("email", ad.getAgent().getEmail()));
        agentLink.getElement().addEventListener("click", event -> ProfileView.cacheUser(ad.getAgent()));
        agentLink.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var layout = new HorizontalLayout(avatar, agentLink);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().setMarginTop("20px").setCursor("pointer");

        add(agentTitle, layout);
    }

}
