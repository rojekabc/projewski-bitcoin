package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WatcherStatistics {
	private String watch;
	public int buyers = 0;
	public int sellers = 0;
	public BigDecimal amountBought = new BigDecimal("0");
	public BigDecimal amountSold = new BigDecimal("0");
	public BigDecimal lastBuyPrice = null;
	public BigDecimal lastSellPrice = null;
	public BigDecimal overallAverage = new BigDecimal("0");
	public BigDecimal buyerAverage = new BigDecimal("0");
	public BigDecimal sellerAverage = new BigDecimal("0");
}
