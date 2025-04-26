package com.dieti.dietiestates25.ui_components;

import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.observers.BidActionListener;
import com.dieti.dietiestates25.services.ad.AdRequestsHandler;
import com.dieti.dietiestates25.services.session.UserSession;
import com.dieti.dietiestates25.views.ad.BidsPanel;
import com.vaadin.flow.component.Key;
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
import lombok.Getter;

public class BidMessage extends VerticalLayout {

    private final AdRequestsHandler adRequestsHandler = new AdRequestsHandler();

    final BidActionListener listener;
    @Getter final private Bid bid;

    private final Button acceptButton, refuseButton, trashButton;
    private Span amount, counterOffer;
    private Anchor accept = null, refuse = null;
    private final HorizontalLayout counterOfferLayout = new HorizontalLayout();

    public BidMessage(Bid bid, String agentEmail, BidActionListener listener) {
        this.bid = bid;
        this.listener = listener;

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

        if (!bid.getOffererMessage().isEmpty())
            message.add(" - " + bid.getOffererMessage());

        var messageLayout = new VerticalLayout(name, message);
        messageLayout.setSpacing(false);
        messageLayout.setPadding(false);

        var timestamp = new Span(bid.getTimestamp());
        timestamp.getStyle().setFontSize("12px").setColor("gray");

        trashButton = new Button("🗑", e -> {
            adRequestsHandler.cancelBid(bid.getId());
            if (listener != null)
                listener.onDeleted(bid);  // Notify deletion
        });

        trashButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        trashButton.setTooltipText("Delete");
        trashButton.getStyle().setCursor("pointer");

        var topLayout = new HorizontalLayout(messageLayout, trashButton);
        topLayout.setWidthFull();
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        acceptButton = new Button("Accept", e -> {
            var success = adRequestsHandler.acceptOrRefuseBid(new Bid.Accept(bid.getId()));
            if (listener != null && success)
                listener.onAccepted(bid);
        });
        acceptButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        acceptButton.getStyle().setCursor("pointer");

        refuseButton = new Button("Refuse", e -> launchCounterOfferDialog(bid, listener));

        refuseButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        refuseButton.getStyle().setCursor("pointer");

        var actionsLayout = new HorizontalLayout(acceptButton, refuseButton);

        add(topLayout, timestamp, actionsLayout);

        if (!bid.getOfferer().equals(UserSession.getEmail()))
            topLayout.remove(trashButton);

        if (!agentEmail.equals(UserSession.getEmail()))
            actionsLayout.removeAll();

        System.out.println(bid.getFirstname());
        System.out.println(bid.getCounteroffer() == null);
        if (bid.getCounteroffer() != null) {
            counterOffer = new Span("Agency counteroffer: " + bid.getCounteroffer().getAmountAsString());
            counterOffer.getStyle()
                    .setColor("#0d6efd")
                    .setFontSize("14px");

            new InfoPopover(counterOffer, bid.getAgentMessage());

            accept = new Anchor("#", "Accept");
            accept.getStyle().setColor("green").setFontSize("14px");
            refuse = new Anchor("#", "Refuse");
            refuse.getStyle().setColor("red").setFontSize("14px");

            counterOfferLayout.setWidthFull();
            counterOfferLayout.add(counterOffer);

            if (bid.getCounteroffer().getStatus().equals("A")) {
                BidsPanel.setOneAccepted(true);
                setCounterOfferAccepted();
            }
            else if (bid.getCounteroffer().getStatus().equals("R"))
                setCounterOfferRefused();
            else {
                if (BidsPanel.isOneAccepted())
                    disableCounterOfferButtons();
                else {
                    if (bid.getOfferer().equals(UserSession.getEmail())) {
                        counterOfferLayout.add(accept, refuse);
                        counterOfferLayout.setAlignSelf(Alignment.END, accept, refuse);

                        accept.getElement().addEventListener("click", event -> {
                            var success = adRequestsHandler.acceptOrRefuseCounterOffer(new Bid.Accept(bid.getCounteroffer().getId()));
                            if (success)
                                setCounterOfferAccepted();
                        }).addEventData("event.preventDefault()");

                        refuse.getElement().addEventListener("click", event -> {
                            var success = adRequestsHandler.acceptOrRefuseCounterOffer(new Bid.Counteroffer.Refuse(bid.getCounteroffer().getId()));
                            if (success)
                                setCounterOfferRefused();
                        }).addEventData("event.preventDefault()");
                    }
                }
            }

            add(new Hr(), counterOfferLayout);
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
        var amount = Form.priceInEuroNumberField("Counteroffer amount");
        amount.setPlaceholder("Leave empty for straight refusal");
        amount.setWidthFull();

        var confirmButton = new Button("Confirm", event -> {
            var success = adRequestsHandler.acceptOrRefuseBid(
                    new Bid.Refuse(
                            bid.getId(),
                            newMessage.getValue(),
                            amount.isEmpty() ? 0 : amount.getValue()
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
        counterofferDialog.add(newMessage, amount);

        counterofferDialog.open();
    }

    public void setCounterOfferAccepted() {
        disableButtons();
        accept.setText("accepted");
        counterOfferLayout.remove(refuse);
        counterOffer.getStyle().setColor("green").setFontWeight(Style.FontWeight.BOLD);
    }

    public void setCounterOfferRefused() {
        disableButtons();
        refuse.setText("refused");
        refuse.setEnabled(false);
        counterOfferLayout.remove(accept);
    }

    public void setAccepted() {
        amount.getStyle().setColor("green");
        disableButtons();
    }

    public void disableButtons() {
        refuseButton.setEnabled(false);
        acceptButton.setEnabled(false);
        trashButton.setEnabled(false);
    }

    public void disableCounterOfferButtons() {
        acceptButton.setEnabled(false);
        refuseButton.setEnabled(false);
    }

    public void setRefused() {
        amount.getStyle().setColor("red").setTextDecoration("line-through");
        disableButtons();
    }
}
