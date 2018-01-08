package pl.projewski.bitcoin.bitbay.api.v2;

import java.util.List;

import lombok.Data;

@Data
public class OrderBook {
	// contains two values per bid. First is a price. Second is an amount.
	private List<String[]> bids;
	private List<String[]> asks;
}
