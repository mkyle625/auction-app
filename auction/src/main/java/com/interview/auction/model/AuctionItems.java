package com.interview.auction.model;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table
public class AuctionItems {
    // Genereate the auction ID, since each is unique
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int auctionId;
    @Column
    private String itemId;
    @Column
    private String itemDescription;
    @Column
    private double currentBid;
    @Column
    private double reservePrice;
    @Column
    private String bidderName;

    // Marked as transient, don't want this to be part of the database
    // This is used for the proper output formatting when returning the model
    @Transient
    private Item item;

    // Getters and setters

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

}
