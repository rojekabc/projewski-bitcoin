package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.util.List;

import lombok.Data;

@Data
public class OrderBook {
	long lastUpdateId;
	List<Order> bids;
	List<Order> asks;
}
