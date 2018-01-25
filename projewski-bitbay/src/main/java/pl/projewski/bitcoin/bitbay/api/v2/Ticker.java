package pl.projewski.bitcoin.bitbay.api.v2;

import lombok.Data;

@Data
public class Ticker {
	// najwieksza wartosc transakcji
	private String max;
	// najmniejsza wartosc transakcji
	private String min;
	private String last;
	// najkorzystniejsza oferta kupna
	private String bid;
	// najkorzystniejsza oferta sprzedarzy
	private String ask;
	// srednia wazona z ostatnich 24h (always same as last)
	private String vwap;
	// srednia z 3 najlepszych ofert sprzedazy (always same as last)
	private String average;
	private String volume;

	private String message;
	private int code;
}
