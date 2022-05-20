package pl.projewski.bitcoin.exchange.api;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderBook {
	List<Order> askOrders = new ArrayList<>();
	List<Order> bidOrders = new ArrayList<>();
}
