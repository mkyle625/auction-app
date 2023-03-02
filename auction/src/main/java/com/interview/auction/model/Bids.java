package com.interview.auction.model;

import jakarta.persistence.*;

@Entity
@Table
public class Bids {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int bidId;
    @Column
    private int auctionItemId;
    @Column
    private String bidderName;
    @Column
    private double maxAutoBidAmount;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(int auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public double getMaxAutoBidAmount() {
        return maxAutoBidAmount;
    }

    public void setMaxAutoBidAmount(double maxAutoBidAmount) {
        this.maxAutoBidAmount = maxAutoBidAmount;
    }

}
