package com.interview.auction.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty
    private String itemId;
    private String description;

    public String getId() {
        return itemId;
    }

    public void setId(String id) {
        this.itemId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
