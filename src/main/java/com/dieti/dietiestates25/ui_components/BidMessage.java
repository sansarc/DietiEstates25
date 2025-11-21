package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.observers.BidActionListener;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.utils.DeleteButton;
import com.dieti.dietiestates25.views.ad.AdView;
import com.dieti.dietiestates25.views.ad.BidsPanel;
import com.dieti.dietiestates25.views.profile.ProfileView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

public class BidMessage extends VerticalLayout implements AfterNavigationObserver {

    public static final String POINTER = "pointer";
    public static final String GREEN = "green";
    public transient AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    @Setter transient BidActionListener listener;
    @Setter @Getter private transient Bid bid;

    @Getter Button acceptButton;
    @Getter Button refuseButton;
    @Getter DeleteButton deleteButton;

    Span amount;
    Span counterOffer;
    Span timestamp;
    Anchor accept;
    Anchor refuse;
    HorizontalLayout topLayout;
    @Getter HorizontalLayout counterOfferLayout;

    public void createBidBaseUI(Bid bid) {
        this.bid = bid;

        setWidthFull();
        setSpacing(false);
        getStyle()
                .setBorder("1px solid #ccc")
                .setBorderRadius("8px")
                .setPadding("10px");

        var name = new Span(bid.getFirstname() + " " + bid.getLastname() + ":");

        amount = new Span(bid.getAmountAsString());
        amount.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var message = new Span(amount);

        if (bid.getOffererMessage() != null && !bid.getOffererMessage().isEmpty())
            message.add(" - " + bid.getOffererMessage());

        var messageLayout = new VerticalLayout(name, message);
        messageLayout.setSpacing(false);
        messageLayout.setPadding(false);

        timestamp = new Span(bid.getTimestamp());
        timestamp.getStyle().setFontSize("12px").setColor("gray");

        deleteButton = new DeleteButton(e -> {
            adRequestsHandler.cancelBid(bid.getId());
            if (listener != null)
                listener.onDeleted(bid);  // Notify deletion
        });

        topLayout = new HorizontalLayout(messageLayout, deleteButton);
        topLayout.setWidthFull();
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
    }

    public BidMessage() {/* for testing */}

    public BidMessage(Bid bid, String agentEmail, BidActionListener listener) {
        this.listener = listener;

        createBidBaseUI(bid);
        addActionButtons(bid);
        manageButtonVisibility(bid, agentEmail);
        handleCounterOfferIfPresent(bid);
    }

    private void addActionButtons(Bid bid) {
        acceptButton = new Button("Accept", e -> {
            boolean success = adRequestsHandler.acceptOrRefuseBid(new Bid.Accept(bid.getId()));
            if (listener != null && success) listener.onAccepted(bid);
        });
        acceptButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        acceptButton.getStyle().setCursor(POINTER);

        refuseButton = new Button("Refuse", e -> launchCounterOfferDialog(bid, listener));
        refuseButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        refuseButton.getStyle().setCursor(POINTER);

        HorizontalLayout actionsLayout = new HorizontalLayout(acceptButton, refuseButton);
        add(topLayout, timestamp, actionsLayout);
    }

    private void manageButtonVisibility(Bid bid, String agentEmail) {
        if (!bid.getOfferer().equals(UserSession.getEmail())) {
            topLayout.remove(deleteButton);
        }

        if (!agentEmail.equals(UserSession.getEmail())) {
            var actionsLayout = getComponentAt(2); // Assuming actionsLayout is always the third added
            if (actionsLayout instanceof HorizontalLayout layout) {
                layout.removeAll();
            }
        }
    }

    private void handleCounterOfferIfPresent(Bid bid) {
        var counter = bid.getCounteroffer();
        if (counter == null) return;

        counterOffer = new Span("Agency counteroffer: " + counter.getAmountAsString());
        counterOffer.getStyle().setColor("#0d6efd").setFontSize("14px");
        new InfoPopover(counterOffer, bid.getAgentMessage());

        counterOfferLayout = new HorizontalLayout(counterOffer);
        counterOfferLayout.setWidthFull();

        switch (counter.getStatus()) {
            case "A" -> {
                BidsPanel.setOneAccepted(true);
                setCounterOfferAccepted();
            }
            case "R" -> setCounterOfferRefused();
            default -> handlePendingCounterOffer(bid, counter);
        }

        add(new Hr(), counterOfferLayout);
    }

    private void handlePendingCounterOffer(Bid bid, Bid.Counteroffer counter) {
        if (BidsPanel.isOneAccepted()) {
            disableCounterOfferButtons();
            return;
        }

        if (!bid.getOfferer().equals(UserSession.getEmail())) return;

        accept = new Anchor("#", "Accept");
        accept.getStyle().setColor(GREEN).setFontSize("14px");

        refuse = new Anchor("#", "Refuse");
        refuse.getStyle().setColor("red").setFontSize("14px");

        counterOfferLayout.add(accept, refuse);
        counterOfferLayout.setAlignSelf(Alignment.END, accept, refuse);

        setupCounterOfferEvents(counter);
    }

    private void setupCounterOfferEvents(Bid.Counteroffer counter) {
        accept.getElement().addEventListener("click", event -> {
            boolean success = adRequestsHandler.acceptOrRefuseCounterOffer(new Bid.Accept(counter.getId()));
            if (success) setCounterOfferAccepted();
        }).addEventData("event.preventDefault()");

        refuse.getElement().addEventListener("click", event -> {
            boolean success = adRequestsHandler.acceptOrRefuseCounterOffer(new Bid.Counteroffer.Refuse(counter.getId()));
            if (success) setCounterOfferRefused();
        }).addEventData("event.preventDefault()");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (UI.getCurrent().getCurrentView() instanceof ProfileView) {
            if (bid.getCounteroffer() != null) {
                var status = new Span(String.format("(%s)", bid.getCounteroffer().getStatus().equals("A") ? "Accepted" : "Refused"));
                status.getStyle().setFontSize("14px").setColor("gray");
                counterOfferLayout.add(status);
            }
            add(new RouterLink("Go to Ad", AdView.class, new RouteParameters("id", String.valueOf(bid.getAdId()))));
        }
    }

    private void launchCounterOfferDialog(Bid bid, BidActionListener listener) {
        var counterofferDialog = new Dialog();
        counterofferDialog.setHeaderTitle("Refuse or make a counteroffer");
        counterofferDialog.setCloseOnEsc(true);
        counterofferDialog.setCloseOnOutsideClick(true);

        var newMessage = new TextField("Optional message to the offerer");
        newMessage.setPlaceholder("Explain your reason or provide context...");
        newMessage.setWidthFull();
        var counterofferAmount = Form.priceInEuroNumberField("Counteroffer amount");
        counterofferAmount.setPlaceholder("Leave empty for straight refusal");
        counterofferAmount.setWidthFull();

        var confirmButton = new Button("Confirm", event -> {
            var success = adRequestsHandler.acceptOrRefuseBid(
                    new Bid.Refuse(
                            bid.getId(),
                            newMessage.getValue(),
                            counterofferAmount.isEmpty() ? 0 : counterofferAmount.getValue()
                    )
            );

            if (success && listener != null) {
                counterofferDialog.close();
                listener.onRefused(bid);
            }

        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);

        var cancelButton = new Button("Cancel", event -> counterofferDialog.close());

        counterofferDialog.getFooter().add(cancelButton, confirmButton);
        counterofferDialog.add(newMessage, counterofferAmount);

        counterofferDialog.open();
    }

    public void setCounterOfferAccepted() {
        disableButtons();
        if (accept != null) {
            accept.setText("accepted");
            counterOfferLayout.remove(refuse);
        }
        counterOffer.getStyle()
                .setColor(GREEN)
                .setFontWeight(Style.FontWeight.BOLD);
    }

    public void setCounterOfferRefused() {
        disableButtons();
        if (refuse != null) {
            refuse.setText("refused");
            refuse.setEnabled(false);
            counterOfferLayout.remove(accept);
        }
    }

    public void setAccepted() {
        amount.getStyle().setColor(GREEN);
        disableButtons();
    }

    public void disableButtons() {
        refuseButton.setEnabled(false);
        acceptButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public void disableCounterOfferButtons() {
        acceptButton.setEnabled(false);
        refuseButton.setEnabled(false);
    }

    public void setRefused() {
        amount.getStyle().setColor("red").setTextDecoration("line-through");
        disableButtons();
    }

    public static BidMessage find(VerticalLayout bidsListLayout, int id) {
        AtomicReference<BidMessage> bidMessage = new AtomicReference<>();

        bidsListLayout.getChildren()
                .forEach(component -> {
                    if (component instanceof BidMessage i && i.getBid().getId() == id)
                            bidMessage.set(i);
                });

        return bidMessage.get();
    }
}
