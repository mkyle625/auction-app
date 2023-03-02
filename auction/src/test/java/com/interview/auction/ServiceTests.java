package com.interview.auction;

import com.interview.auction.model.AuctionItems;
import com.interview.auction.model.Bids;
import com.interview.auction.model.Item;
import com.interview.auction.service.AuctionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class ServiceTests {

    @Autowired
    private AuctionService auctionService;

    @Test
    void serviceAddAuctionItem() {
        AuctionItems mockAuctionItem = new AuctionItems();
        Item item = new Item();
        item.setId("test");
        item.setDescription("description");
        mockAuctionItem.setAuctionId(1);
        mockAuctionItem.setReservePrice(100);
        mockAuctionItem.setItem(item);

        auctionService.saveOrUpdate(mockAuctionItem);

        assertThat(auctionService.getAuctionItemById(1).getAuctionId(), is(1));

    }

    @Test
    void serviceAddMultipleAuctionItems() {
        for (int i = 1; i < 4; i++) {
            AuctionItems mockAuctionItem = new AuctionItems();
            Item item = new Item();
            item.setId("test");
            item.setDescription("description");
            mockAuctionItem.setAuctionId(i);
            mockAuctionItem.setReservePrice(100);
            mockAuctionItem.setItem(item);

            auctionService.saveOrUpdate(mockAuctionItem);
        }

        assertThat(auctionService.getAllAuctionItems().size(), is(3));

    }

    @Test
    void serviceGetMultipleBids() {
        List<Bids> test_list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Bids mockBid = new Bids();
            mockBid.setBidId(1);
            mockBid.setMaxAutoBidAmount(102);
            mockBid.setAuctionItemId(1);
            mockBid.setBidderName("Another test");
            test_list.add(mockBid);

            auctionService.saveOrUpdate(mockBid);
        }

        assertThat(auctionService.getAllBids().size(), is(1));

    }
}
