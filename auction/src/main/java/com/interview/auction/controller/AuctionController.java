package com.interview.auction.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.interview.auction.model.*;
import com.interview.auction.service.AuctionService;

@RestController
public class AuctionController {
    @Autowired
    AuctionService auctionService;

    /**
     * GET endpoint for Auction Items.
     * Method processes items from the repository to be exported to JSON
     * @return All auction items in JSON format
     */
    @GetMapping("/auctionItems")
    private List<Map<String, Object>> getAllAuctionItems() {
        // Format the response. Exclude blank values, format the item correctly

        // Temporary lists (from the repository)
        List<AuctionItems> all_auction_items = auctionService.getAllAuctionItems();
        List<Map<String, Object>> return_list = new ArrayList<>();

        // For each item, we are going to add it to our return map
        for (AuctionItems item : all_auction_items) {
            Map<String, Object> temp_map = new HashMap<String, Object>();
            temp_map.put("auctionId", item.getAuctionId());
            temp_map.put("currentBid", item.getCurrentBid());
            temp_map.put("reservePrice", item.getReservePrice());
            if (item.getBidderName() != null) {
                temp_map.put("bidderName", item.getBidderName());
            }
            temp_map.put("item", item.getItem());
            return_list.add(temp_map);
        }
        return return_list;

    }

    /***
     * GET endpoint for Auction Items by ID
     * @param id The ID to get
     * @return Auction item with the specified ID, in JSON format
     */
    @GetMapping("/auctionItems/{auctionItemId}")
    private AuctionItems getAuctionItems(@PathVariable("auctionItemId") int id) {
        return auctionService.getAuctionItemById(id);
    }

    /***
     * POST endpoint to add an auction item
     * @param auctionItems JSON model of the auction item to add
     * @return The auction item ID upon creation
     */
    @PostMapping(value = "/auctionItems", produces = MediaType.APPLICATION_JSON_VALUE)
    private Map<String, String> saveAuctionItem(@RequestBody AuctionItems auctionItems)
    {
        // This sets the sub-item to be equal to the ID we specified in the POST request
        auctionItems.setItemId(auctionItems.getItem().getId());
        auctionItems.setItemDescription(auctionItems.getItem().getDescription());
        auctionService.saveOrUpdate(auctionItems);

        return Collections.singletonMap("auctionItemId", String.valueOf(auctionItems.getAuctionId()));
    }

    /***
     * POST endpoint for bids
     * @param bids JSON model of bid to be placed
     * @return A status, based on the auction activity in JSON format
     */
    @PostMapping("/bids")
    private Map<String, String> addBid(@RequestBody Bids bids) {
        try {
            AuctionItems auctionItems = auctionService.getAuctionItemById(bids.getAuctionItemId());
            List<Bids> all_bids = auctionService.getAllBids();

            // Check if there is no bids
            if (auctionItems.getCurrentBid() == 0.0) {
                // In this case, we can just bid on it
                auctionService.saveOrUpdate(bids);
                auctionItems.setBidderName(bids.getBidderName());

                // Check if the amount is lower than the reserve
                if (bids.getMaxAutoBidAmount() < auctionItems.getReservePrice()) {
                    auctionItems.setCurrentBid(bids.getMaxAutoBidAmount());
                    auctionService.saveOrUpdate(auctionItems);
                    return Collections.singletonMap("response", "Bid placed, but the reserve price has not yet been met.");
                } else {
                    auctionItems.setCurrentBid(auctionItems.getReservePrice());
                    auctionService.saveOrUpdate(auctionItems);
                    return Collections.singletonMap("response", "Bid placed, and the reserve has been met");
                }

            }

            // Check if this bid is higher than the current bid
            if (bids.getMaxAutoBidAmount() > auctionItems.getCurrentBid()) {
                // Bid on it ...
                auctionService.saveOrUpdate(bids);
                auctionItems.setBidderName(bids.getBidderName());

                auctionItems.setCurrentBid(bids.getMaxAutoBidAmount());
                auctionService.saveOrUpdate(auctionItems);

                // ... and see if we get out-bid
                for (Bids bid : all_bids) {
                    // Check if the bid was on this auction
                    if (bid.getAuctionItemId() == auctionItems.getAuctionId()) {
                        // Loop through all bids and calculate the new current bid

                        // Check if bid has a higher max
                        if (bid.getMaxAutoBidAmount() > bids.getMaxAutoBidAmount()) {
                            auctionItems.setCurrentBid(bids.getMaxAutoBidAmount() + 1);
                            auctionService.saveOrUpdate(auctionItems);
                            if (bids.getMaxAutoBidAmount() < auctionItems.getReservePrice()) {
                                return Collections.singletonMap("response", "Bid placed, you were outbid by someone! The reserve price has not yet been met.");
                            } else {
                                return Collections.singletonMap("response", "Bid placed, you were outbid by someone!");
                            }
                        } else {
                            if (bids.getMaxAutoBidAmount() < auctionItems.getReservePrice()) {
                                return Collections.singletonMap("response", "Bid placed, and you are currently the winner! You outbid " + bid.getBidderName() + "! The reserve price has not yet been met");
                            } else {
                                return Collections.singletonMap("response", "Bid placed, and you are currently the winner! You outbid " + bid.getBidderName());
                            }
                        }

                    }
                }

            } else {
                return Collections.singletonMap("response", "Attempted bid of " + bids.getMaxAutoBidAmount() + " is lower than (or equal to) the current bid price of " + auctionItems.getCurrentBid());
            }

        } catch (Exception e) {
            return Collections.singletonMap("response", "Auction ID of " + bids.getAuctionItemId() + " not found!");
        }

        // If we reach this without returning anything from above, there is probably a logic error somewhere :)
        return Collections.singletonMap("response", "Unknown error");
    }


}
