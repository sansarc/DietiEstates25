package com.dieti.dietiestates25.observers;

import com.dieti.dietiestates25.dto.bid.Bid;

public interface BidActionListener {
    void onAccepted(Bid bid);
    void onRefused(Bid bid);
    void onDeleted(Bid bid);
}
