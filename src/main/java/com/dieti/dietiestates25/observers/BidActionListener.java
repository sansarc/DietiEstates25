package com.dieti.dietiestates25.observers;

import com.dieti.dietiestates25.dto.bid.Bid;
import org.slf4j.LoggerFactory;

public interface BidActionListener {
    void onAccepted(Bid bid);
    void onRefused(Bid bid);
    void onDeleted(Bid bid);

    default void onAdded(Bid bid, String agentEmail) {
        LoggerFactory.getLogger(BidActionListener.class).info("Default implementation called.");
    }
}
