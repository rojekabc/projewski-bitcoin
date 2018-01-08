package pl.projewski.bitcoin.ui.terminal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatTableElement {
	private int fieldSize;
	private String fieldTitle;
	private String statFieldName;
}
