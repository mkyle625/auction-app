package com.interview.auction.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    // This class is not part of the database, rather it is used for
    // proper formatting of the output in JSON
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
