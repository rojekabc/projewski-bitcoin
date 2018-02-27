package pl.projewski.bitcoin.exchange.api;

import java.util.List;

import lombok.Data;

@Data
public class OrderBook {
	List<Order> askOrders;
	List<Order> bidOrders;
}
