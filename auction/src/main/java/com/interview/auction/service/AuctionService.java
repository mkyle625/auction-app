package com.interview.auction.service;

import com.interview.auction.model.AuctionItems;
import com.interview.auction.model.Bids;
import com.interview.auction.model.Item;
import com.interview.auction.repository.AuctionRepository;
import com.interview.auction.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    BidRepository bidRepository;

    /***
     * Constructor that is used for Unit testing. Allows the use of repositories while doing Unit tests
     * @param auctionRepository Reference to the auction repository
     * @param bidRepository Reference to the bid repository
     */
    public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    /***
     * Gets all the auction items from the repository. Because of the storage format in the database, we need to set
     * the actual item in the auction item in order to display/format the response correctly
     * @return List of auction items
     */
    public List<AuctionItems> getAllAuctionItems() {
        List<AuctionItems> auctionItems= new ArrayList<AuctionItems>();
        auctionRepository.findAll().forEach(item -> {
            Item temp_item = new Item();
            temp_item.setDescription(item.getItemDescription());
            temp_item.setId(item.getItemId());
            item.setItem(temp_item);
            auctionItems.add(item);
        });
        return auctionItems;
    }

    /***
     * Gets a list of all the current bids from the repository. This is used in the POST method to compare new or
     * old bids, and compute if someone is outbid or not
     * @return List of bids
     */
    public List<Bids> getAllBids() {
        List<Bids> bids = new ArrayList<Bids>();
        bidRepository.findAll().forEach(item -> bids.add(item));
        return bids;
    }

    /***
     * Gets an auction item object with a specific ID from the repository
     * @param id ID to get
     * @return Auction item object corresponding with the ID
     */
    public AuctionItems getAuctionItemById(int id) {
        return auctionRepository.findById(id).get();
    }

    /***
     * Main method to add or update auction items in the repository.
     * @param auctionItems The auction item to add, or update
     */
    public void saveOrUpdate(AuctionItems auctionItems) {
        auctionRepository.save(auctionItems);
    }

    /***
     * Main method to add or update bids in the repository.
     * @param bid The bid to add, or update
     */
    public void saveOrUpdate(Bids bid) {
        bidRepository.save(bid);
    }

}
