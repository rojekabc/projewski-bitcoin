package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.util.List;

import lombok.Data;

@Data
public class Symbol {
	private String symbol;
	private SymbolStatusType status;
	private String baseAsset;
	private short baseAssetPrecision;
	private String quoteAsset;
	private short quotePrecision;
	private List<OrderType> orderTypes;
	private boolean icebergAllowed;
	private List<Filter> filters;
}
