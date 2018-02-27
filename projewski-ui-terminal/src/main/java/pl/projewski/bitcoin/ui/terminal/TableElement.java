package pl.projewski.bitcoin.ui.terminal;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.projewski.bitcoin.common.EStatisticState;

@Data
@AllArgsConstructor
public class TableElement {
    String data;
    String ansiColor;
    EStatisticState state;
}
