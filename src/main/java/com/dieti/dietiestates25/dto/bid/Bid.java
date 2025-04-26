package com.dieti.dietiestates25.dto.bid;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Bid {

    @SerializedName("ad") protected int adId;
    protected double amount;
    protected String agentMessage;
    protected String offererMessage;
    private String firstname, lastname;

    @SerializedName("counterOffer") private Counteroffer counteroffer;

    protected int id;
    protected String status;
    protected String offerer;
    private String timestamp;

    protected Bid() {}

    public static class Refuse extends Bid {
        public Refuse(int id, String agentMessage, double amount) {
            this.id = id;
            this.amount = amount;
            this.agentMessage = agentMessage;
            this.status = "R";
        }
    }

    public static class Accept extends Bid {
        public Accept(int id) {
            this.id = id;
            this.status = "A";
        }
    }

    public static class Insert extends Bid {
        public Insert(int adId, double amount, String offererMessage, String agentMessage) {
            this.adId = adId;
            this.amount = amount;
            this.offererMessage = offererMessage;
            this.agentMessage = agentMessage;
        }
    }

    @Getter
    public static class Counteroffer extends Bid {
        int parentBidId;

        public static class Refuse extends Counteroffer {
            public Refuse(int id) {
                this.id = id;
                this.status = "R";
            }
        }
    }

    public String getAmountAsString() {
        return String.format("%.2f", amount) + "€";
    }

    public String getTimestamp() {
        var  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var localDateTime = LocalDateTime.parse(timestamp.split("\\.")[0], formatter);
        var instant = localDateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(localDateTime));

        return instant.toString().split("T")[0] + " " + instant.toString().split("T")[1].substring(0, 5);
    }
}
