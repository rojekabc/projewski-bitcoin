package pl.projewski.bitcoin.ui.terminal;

import org.apache.commons.lang3.StringUtils;
import pl.projewski.bitcoin.common.EStatisticState;
import pl.projewski.bitcoin.common.StateValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TerminalTable {
    private String title;
    private int tableLength = 1;
    private final List<String> headers = new ArrayList<>();
    private LinkedList<List<TableElement>> rows = new LinkedList<>();

    public TerminalTable reset() {
        rows = new LinkedList<>();
        return this;
    }

    public TerminalTable title(final String tableTitle) {
        title = ' ' + tableTitle + ' ';
        return this;
    }

    public TerminalTable header(final String headerName) {
        return header(headerName, headerName.length() + 2);
    }

    public TerminalTable header(final String headerName, final int columnLength) {
        tableLength += columnLength;
        tableLength++; // separator
        if (headerName.length() < columnLength) {
            headers.add(StringUtils.center(headerName, columnLength, ' '));
        } else {
            headers.add(StringUtils.center(headerName.substring(0, columnLength), columnLength, ' '));
        }
        return this;
    }

    public TerminalTable data(final StateValue stateValue) {
        return data(stateValue.value().toString(), AnsiConstants.FOREGROUNG_GRAY, stateValue.state());
    }

    public TerminalTable data(final StateValue stateValue, final String ansiColor) {
        return data(stateValue.value().toString(), ansiColor, stateValue.state());
    }

    public TerminalTable data(final String data) {
        return data(data, AnsiConstants.FOREGROUNG_GRAY, null);
    }

    public TerminalTable data(final BigDecimal data) {
        return data(data == null ? "" : data.toString(), AnsiConstants.FOREGROUNG_GRAY, null);
    }

    public TerminalTable data(final BigDecimal data, final String ansiColor) {
        return data(data == null ? "" : data.toString(), ansiColor, null);
    }

    public TerminalTable data(final String data, final String ansiColor) {
        return data(data, ansiColor, null);
    }

    public TerminalTable data(final String data, final String ansiColor, final EStatisticState state) {
        final TableElement element = new TableElement(data, ansiColor, state);
        List<TableElement> row;
        if (rows.isEmpty() || rows.getLast().size() == headers.size()) {
            row = new ArrayList<>();
            rows.add(row);
        }
        row = rows.getLast();
        row.add(element);
        return this;
    }

    public void draw() {
        StringBuilder stringBuilder;
        // title
        stringBuilder = new StringBuilder();
        stringBuilder.append('+');
        if (title != null) {
            stringBuilder.append(StringUtils.center(title, tableLength - 2, '-'));
        }
        stringBuilder.append('+');
        System.out.print(AnsiConstants.FOREGROUNG_GRAY);
        System.out.println(stringBuilder.toString());
        // header
        stringBuilder = new StringBuilder();
        stringBuilder.append('|');
        for (final String header : headers) {
            stringBuilder.append(header).append('|');
        }
        System.out.print(AnsiConstants.FOREGROUNG_GRAY);
        System.out.println(stringBuilder.toString());
        // separator
        stringBuilder = new StringBuilder();
        stringBuilder.append('+');
        for (final String header : headers) {
            stringBuilder.append(StringUtils.repeat('-', header.length())).append('+');
        }
        System.out.print(AnsiConstants.FOREGROUNG_GRAY);
        System.out.println(stringBuilder.toString());
        // data rows
        for (final List<TableElement> row : rows) {
            // single row
            stringBuilder = new StringBuilder();
            stringBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
            stringBuilder.append('|');
            for (int i = 0; i < headers.size(); i++) {
                final int length = headers.get(i).length();
                // row element
                if (row.size() > i) {
                    final TableElement element = row.get(i);
                    final EStatisticState state = element.getState();
                    final String stateString = state == null ? null : getStateString(state);
                    final int stateStringLength = state == null ? 0 : 2;
                    final int dataLen = element.getData()
                            .length() > length - stateStringLength ? length - stateStringLength : element.getData()
                            .length();
                    stringBuilder.append(element.ansiColor == null ? "" : element.ansiColor)
                            .append(StringUtils.leftPad(element.getData().substring(0, dataLen),
                                    length - stateStringLength, ' '))
                            .append(stateString == null ? "" : stateString);
                } else {
                    stringBuilder.append(StringUtils.repeat(' ', length));
                }
                stringBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
                stringBuilder.append('|');
            }
            System.out.println(stringBuilder.toString());
        }
    }

    private String getStateString(final EStatisticState state) {
        if (state != null) {
            switch (state) {
                case MINUS:
                    return AnsiConstants.FOREGROUNG_GRAY + " -";
                case PLUS:
                    return AnsiConstants.FOREGROUNG_GRAY + " +";
                case ZERO:
                    return AnsiConstants.FOREGROUNG_GRAY + "  ";
            }
        }
        return "  ";
    }


    // TODO: MOVE TO TEST
    public static void main(final String[] args) {
        final TerminalTable table = new TerminalTable();
        table.title("Table")
                .header("Alfa").header("Beta", 10).header("Gamma").header("Anabolik", 6) //
                .data("12345678").data("1.0", null, EStatisticState.PLUS)
                .data("Alkoholowo", AnsiConstants.FOREGROUNG_GREEN, EStatisticState.MINUS) //
                .draw();
    }
}
