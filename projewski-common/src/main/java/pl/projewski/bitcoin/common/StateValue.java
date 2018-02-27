package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

public class StateValue {
	private BigDecimal value;
	private EStatisticState state;

	public BigDecimal value() {
		return value == null ? BigDecimal.ZERO : value;
	}

	public EStatisticState state() {
		return state;
	}

	public void newValue(final BigDecimal newValue) {
		if (value == null) {
			state = EStatisticState.ZERO;
		} else {
			final int compare = value.compareTo(newValue);
			if (compare == 0) {
				state = EStatisticState.ZERO;
			} else if (compare > 0) {
				state = EStatisticState.MINUS;
			} else {
				state = EStatisticState.PLUS;
			}
		}
		value = newValue;
	}

	public int compareTo(BigDecimal bd) {
		return this.value.compareTo(bd);
	}
}
