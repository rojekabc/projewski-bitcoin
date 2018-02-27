package pl.projewski.bitcoin.exchange.bitbay.api.v2;

import lombok.Data;

import java.util.List;

@Data
public class OrderBook {
    private List<Order> bids;
    private List<Order> asks;
}
