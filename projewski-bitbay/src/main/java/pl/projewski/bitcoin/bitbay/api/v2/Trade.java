package pl.projewski.bitcoin.bitbay.api.v2;

import lombok.Data;

@Data
public class Trade {
	private String date;
	private String price;
	private String amount;
	private String tid;
	private String type;
}
