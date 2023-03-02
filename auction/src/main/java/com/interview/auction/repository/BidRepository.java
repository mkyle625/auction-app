package com.interview.auction.repository;

import org.springframework.data.repository.CrudRepository;
import com.interview.auction.model.*;

public interface BidRepository extends CrudRepository<Bids, Integer>  {

}
