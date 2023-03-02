package com.interview.auction;

import com.interview.auction.controller.AuctionController;
import com.interview.auction.model.AuctionItems;
import com.interview.auction.model.Bids;
import com.interview.auction.model.Item;
import com.interview.auction.service.AuctionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuctionController.class)
class AuctionApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuctionService auctionService;

	@Test
	void contextLoads() {
	}

	/***
	 * Tests the POST auction endpoint with a single mock auction
	 * Should return the auction with an ID of 1
	 * @throws Exception
	 */
	@Test
	void postAuctionItem() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auctionItems")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"reservePrice\": 10450.0,\n" +
						"    \"item\": {\n" +
						"        \"itemId\": \"test\",\n" +
						"        \"description\": \"description\"\n" +
						"    }\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(HttpStatus.OK.value(), is(response.getStatus()));

	}

	/***
	 * Tests the GET auction endpoint to get a list of all the auction items
	 * Should return a list of auction items
	 * @throws Exception
	 */
	@Test
	void getAuctionItems() throws Exception {
		List<AuctionItems> test_list = new ArrayList<AuctionItems>();
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);
		test_list.add(mockAuctionItem);
		Mockito.when(auctionService.getAllAuctionItems()).thenReturn(test_list);

		String expected = "[{\"auctionId\":1,\"reservePrice\":100.0,\"item\":{\"itemId\":\"test\",\"description\":\"description\",\"id\":\"test\"},\"currentBid\":0.0}]";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auctionItems").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		assertThat(expected, is(result.getResponse().getContentAsString()));

	}

	/***
	 * Tests the POST bid endpoint, with a bid that is above the reserve
	 * Should return a response with: "Bid placed, and the reserve has been met"
	 * @throws Exception
	 */
	@Test
	void postBidOverReserve() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);

		String expected = "{\"response\":\"Bid placed, and the reserve has been met\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 9502.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());

		assertThat(expected, is(result.getResponse().getContentAsString()));
	}

	/***
	 * Tests the POST bid endpoint, with a bid that is under the reserve
	 * Should return a response with "Bid placed, but the reserve price has not yet been met."
	 * @throws Exception
	 */
	@Test
	void postBidUnderReserve() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);

		String expected = "{\"response\":\"Bid placed, but the reserve price has not yet been met.\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 30.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());

		assertThat(expected, is(result.getResponse().getContentAsString()));
	}

	/***
	 * Tests the POST bid endpoint with a bid that is lower than the current bid price.
	 * Should return: "Attempted bid of 99.0 is lower than (or equal to) the current bid price of 100.0"
	 * @throws Exception
	 */
	@Test
	void postBidTooLow() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);

		String expected = "{\"response\":\"Attempted bid of 99.0 is lower than (or equal to) the current bid price of 100.0\"}";

		RequestBuilder requestBuilder_firstBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 9502.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		RequestBuilder requestBuilder_secondBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 99.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result_firstBid = mockMvc.perform(requestBuilder_firstBid).andReturn();

		MvcResult result_secondBid = mockMvc.perform(requestBuilder_secondBid).andReturn();

		assertThat(expected, is(result_secondBid.getResponse().getContentAsString()));
	}

	/***
	 * Tests the POST bid method, with two bids and one bid outbidding the previous
	 * Should return: "Bid placed, and you are currently the winner! You outbid Another test"
	 * @throws Exception
	 */
	@Test
	void postBidOutbid() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		List<Bids> test_list = new ArrayList<>();
		Bids mockBid = new Bids();
		mockBid.setBidId(1);
		mockBid.setMaxAutoBidAmount(102);
		mockBid.setAuctionItemId(1);
		mockBid.setBidderName("Another test");
		test_list.add(mockBid);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);
		Mockito.when(auctionService.getAllBids()).thenReturn(test_list);

		String expected = "{\"response\":\"Bid placed, and you are currently the winner! You outbid Another test\"}";

		RequestBuilder requestBuilder_firstBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 102.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		RequestBuilder requestBuilder_secondBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 105.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result_firstBid = mockMvc.perform(requestBuilder_firstBid).andReturn();

		MvcResult result_secondBid = mockMvc.perform(requestBuilder_secondBid).andReturn();

		assertThat(expected, is(result_secondBid.getResponse().getContentAsString()));
	}

	/***
	 * Tests the POST bid endpoint, with two bids and one getting outbid by a new maximum
	 * Should return: "Bid placed, you were outbid by someone!"
	 * @throws Exception
	 */
	@Test
	void postBidGetOutbid() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(100);
		mockAuctionItem.setItem(item);

		List<Bids> test_list = new ArrayList<>();
		Bids mockBid = new Bids();
		mockBid.setBidId(1);
		mockBid.setMaxAutoBidAmount(102);
		mockBid.setAuctionItemId(1);
		mockBid.setBidderName("Another test");
		test_list.add(mockBid);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);
		Mockito.when(auctionService.getAllBids()).thenReturn(test_list);

		String expected = "{\"response\":\"Bid placed, you were outbid by someone!\"}";

		RequestBuilder requestBuilder_firstBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 102.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		RequestBuilder requestBuilder_secondBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 101.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result_firstBid = mockMvc.perform(requestBuilder_firstBid).andReturn();

		MvcResult result_secondBid = mockMvc.perform(requestBuilder_secondBid).andReturn();

		assertThat(expected, is(result_secondBid.getResponse().getContentAsString()));
	}

	/***
	 * Tests the POST bid method, but with two bids both under the reserve (and one outbidding the other)
	 * Should return: "Bid placed, and you are currently the winner! You outbid Another test! The reserve price has not yet been met"
	 * @throws Exception
	 */
	@Test
	void postBidMultiUnderReserve() throws Exception {
		AuctionItems mockAuctionItem = new AuctionItems();
		Item item = new Item();
		item.setId("test");
		item.setDescription("description");
		mockAuctionItem.setAuctionId(1);
		mockAuctionItem.setReservePrice(200);
		mockAuctionItem.setItem(item);

		List<Bids> test_list = new ArrayList<>();
		Bids mockBid = new Bids();
		mockBid.setBidId(1);
		mockBid.setMaxAutoBidAmount(102);
		mockBid.setAuctionItemId(1);
		mockBid.setBidderName("Another test");
		test_list.add(mockBid);

		Mockito.when(auctionService.getAuctionItemById(1)).thenReturn(mockAuctionItem);
		Mockito.when(auctionService.getAllBids()).thenReturn(test_list);

		String expected = "{\"response\":\"Bid placed, and you are currently the winner! You outbid Another test! The reserve price has not yet been met\"}";

		RequestBuilder requestBuilder_firstBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 102.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		RequestBuilder requestBuilder_secondBid = MockMvcRequestBuilders.post("/bids")
				.accept(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"auctionItemId\": 1,\n" +
						"    \"maxAutoBidAmount\": 104.0,\n" +
						"    \"bidderName\": \"ABC Test\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result_firstBid = mockMvc.perform(requestBuilder_firstBid).andReturn();

		MvcResult result_secondBid = mockMvc.perform(requestBuilder_secondBid).andReturn();

		assertThat(expected, is(result_secondBid.getResponse().getContentAsString()));
	}

}
