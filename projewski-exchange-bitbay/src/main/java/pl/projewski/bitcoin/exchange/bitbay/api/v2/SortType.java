package pl.projewski.bitcoin.exchange.bitbay.api.v2;

public enum SortType {
	DESC("desc"), ASC("asc");

	private String value;

	private SortType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
