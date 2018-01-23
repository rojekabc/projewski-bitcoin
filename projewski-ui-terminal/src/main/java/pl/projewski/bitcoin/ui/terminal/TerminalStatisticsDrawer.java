package pl.projewski.bitcoin.ui.terminal;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import pl.projewski.bitcoin.common.EStatisticState;
import pl.projewski.bitcoin.common.IStatistics;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;

class TerminalStatisticsDrawer implements IStatisticsDrawer {
	private final static StatTableElement[] TABLE_ELEMENTS = { //
	        new StatTableElement(10, "Watch", "watch"), //
	        new StatTableElement(8, "Buys", "buyers"), //
	        new StatTableElement(8, "Sells", "sellers"), //
	        new StatTableElement(23, "Bought", "amountBought"), //
	        new StatTableElement(23, "Sold", "amountSold"), //
	        new StatTableElement(13, "Last buy", "lastBuyPrice"), //
	        new StatTableElement(13, "Last sell", "lastSellPrice"), //
	        new StatTableElement(13, "Avg buy", "buyerAverage"), //
	        new StatTableElement(13, "Avg sell", "sellerAverage") //
	};

	private final static StatTableElement[] TX_TABLE_ELEMENTS = { //
	        new StatTableElement(5, "Id", "config.id"), //
	        new StatTableElement(5, "Coin", "config.cryptoCoin"), //
	        new StatTableElement(10, "Invest", "config.invest"), //
	        new StatTableElement(10, "Buy price", "config.buyPrice"), //
	        new StatTableElement(10, "Stop price", "config.stopPrice"), //
	        new StatTableElement(10, "Zero price", "config.zeroPrice"), //
	        new StatTableElement(10, "Target price", "config.targetPrice"), //
	        new StatTableElement(10, "Move stop price", "moveStopPrice") //
	};

	private final List<WatcherStatistics> watcherStats = new ArrayList<>();
	private final List<TransactionStatistics> txStats = new ArrayList<>();

	private void updateStatistics() {
		System.out.print(AnsiConstants.CLEAR_SCREEN); // clearscreen
		System.out.print(AnsiConstants.GOTO_BEGIN); // gotoxy 0,0

		System.out.println("--- Watchers ---");
		drawWatcherTable(TABLE_ELEMENTS, watcherStats);

		System.out.println();
		System.out.println("--- Transactions ---");
		drawTransactionTable(TX_TABLE_ELEMENTS, txStats);

		System.out.println("Last update time " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
		System.out.println("Press <Enter> to put command");
	}

	@Override
	public void informException(final Exception e) {
		System.out.print(AnsiConstants.CLEAR_SCREEN); // clearscreen
		System.out.print(AnsiConstants.GOTO_BEGIN); // gotoxy 0,0

		e.printStackTrace();

		System.out.println("Last update time " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
		System.out.println("Press <Enter> to put command");
	}

	private void drawWatcherTable(final StatTableElement[] tableElements, final List<WatcherStatistics> statsArray) {
		final StringBuilder lineBuilder = new StringBuilder();
		// title
		lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
		lineBuilder.append('|');
		for (final StatTableElement element : tableElements) {
			lineBuilder.append(AnsiConstants.FOREGROUNG_WHITE);
			int fieldSize = element.getFieldSize();
			final String fieldValue = element.getFieldTitle();
			fieldSize -= fieldValue.length();
			while (fieldSize > 0) {
				lineBuilder.append(' ');
				fieldSize--;
			}
			lineBuilder.append(fieldSize >= 0 ? fieldValue : fieldValue.substring(0, fieldValue.length() + fieldSize));
			lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
			lineBuilder.append('|');
		}
		lineBuilder.append(System.lineSeparator());
		// line
		lineBuilder.append('+');
		for (final StatTableElement element : tableElements) {
			int fieldSize = element.getFieldSize();
			while (fieldSize > 0) {
				lineBuilder.append('-');
				fieldSize--;
			}
			lineBuilder.append('+');
		}
		lineBuilder.append(System.lineSeparator());
		// statistics
		for (final WatcherStatistics stats : statsArray) {
			lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
			lineBuilder.append('|');
			for (final StatTableElement element : tableElements) {
				int fieldSize = element.getFieldSize();

				String fieldValue = null;
				try {
					final String[] statFieldNameSplit = element.getStatFieldName().split("\\.");
					final PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
					Object object = stats;
					for (final String splited : statFieldNameSplit) {
						object = propertyUtils.getNestedProperty(object, splited);
					}
					fieldValue = object == null ? null : object.toString();

					// fieldValue = BeanUtils.getProperty(stats,
					// element.getStatFieldName());
					fieldSize -= fieldValue.length();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
				}
				String state = null;
				switch (element.getStatFieldName()) {
				case "buyers":
					state = getStateLine(stats.buyersState);
					break;
				case "sellers":
					state = getStateLine(stats.sellersState);
					break;
				case "amountBought":
					state = getStateLine(stats.boughtState);
					break;
				case "amountSold":
					state = getStateLine(stats.soldState);
					break;
				case "lastBuyPrice":
					state = getStateLine(stats.buyPriceState);
					break;
				case "lastSellPrice":
					state = getStateLine(stats.sellPriceState);
					break;
				case "buyerAverage":
					state = getStateLine(stats.buyAvgState);
					break;
				case "sellerAverage":
					state = getStateLine(stats.sellAvgState);
					break;
				}
				if (state != null) {
					fieldSize -= 2;
				}
				while (fieldSize > 0) {
					lineBuilder.append(' ');
					fieldSize--;
				}
				String color = AnsiConstants.FOREGROUNG_GRAY;
				switch (element.getStatFieldName()) {
				case "buyers":
					if (stats.buyers > stats.sellers) {
						color = AnsiConstants.FOREGROUNG_GREEN;
					}
					break;
				case "sellers":
					if (stats.sellers > stats.buyers) {
						color = AnsiConstants.FOREGROUNG_RED;
					}
					break;
				case "amountBought":
					if (stats.amountBought.compareTo(stats.amountSold) > 0) {
						color = AnsiConstants.FOREGROUNG_GREEN;
					}
					break;
				case "amountSold":
					if (stats.amountSold.compareTo(stats.amountBought) > 0) {
						color = AnsiConstants.FOREGROUNG_RED;
					}
					break;
				}
				lineBuilder.append(color);
				lineBuilder
				        .append(fieldSize >= 0 ? fieldValue : fieldValue.substring(0, fieldValue.length() + fieldSize));
				if (state != null) {
					lineBuilder.append(state);
				}
				lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
				lineBuilder.append('|');
			}
			lineBuilder.append(System.lineSeparator());
		}
		lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
		System.out.println(lineBuilder.toString());
	}

	private String getStateLine(final EStatisticState state) {
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

	private void drawTransactionTable(final StatTableElement[] tableElements,
	        final List<TransactionStatistics> statsArray) {
		final StringBuilder lineBuilder = new StringBuilder();
		// title
		lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
		lineBuilder.append('|');
		for (final StatTableElement element : tableElements) {
			lineBuilder.append(AnsiConstants.FOREGROUNG_WHITE);
			int fieldSize = element.getFieldSize();
			final String fieldValue = element.getFieldTitle();
			fieldSize -= fieldValue.length();
			while (fieldSize > 0) {
				lineBuilder.append(' ');
				fieldSize--;
			}
			lineBuilder.append(fieldSize >= 0 ? fieldValue : fieldValue.substring(0, fieldValue.length() + fieldSize));
			lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
			lineBuilder.append('|');
		}
		lineBuilder.append(System.lineSeparator());
		// line
		lineBuilder.append('+');
		for (final StatTableElement element : tableElements) {
			int fieldSize = element.getFieldSize();
			while (fieldSize > 0) {
				lineBuilder.append('-');
				fieldSize--;
			}
			lineBuilder.append('+');
		}
		lineBuilder.append(System.lineSeparator());
		// statistics
		for (final TransactionStatistics stats : statsArray) {
			lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
			lineBuilder.append('|');
			for (final StatTableElement element : tableElements) {
				int fieldSize = element.getFieldSize();

				String fieldValue = null;
				try {
					final String[] statFieldNameSplit = element.getStatFieldName().split("\\.");
					final PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
					Object object = stats;
					for (final String splited : statFieldNameSplit) {
						object = propertyUtils.getNestedProperty(object, splited);
					}
					fieldValue = object == null ? null : object.toString();

					if (fieldValue != null) {
						fieldSize -= fieldValue.length();
					}
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
				}
				while (fieldSize > 0) {
					lineBuilder.append(' ');
					fieldSize--;
				}
				String color = AnsiConstants.FOREGROUNG_GRAY;
				switch (element.getStatFieldName()) {
				case "config.targetPrice":
					if (stats.getTargetAlarm() != null && stats.getTargetAlarm()) {
						color = AnsiConstants.FOREGROUNG_GREEN;
					}
					break;
				case "config.zeroPrice":
					if (stats.getZeroAlarm() != null && stats.getZeroAlarm()) {
						color = AnsiConstants.FOREGROUNG_GREEN;
					}
					break;
				case "config.stopPrice":
					if (stats.getStopAlarm() != null && stats.getStopAlarm()) {
						color = AnsiConstants.FOREGROUNG_RED;
					}
					break;
				case "moveStopPrice":
					if (stats.getMoveStopAlarm() != null && stats.getMoveStopAlarm()) {
						color = AnsiConstants.FOREGROUNG_RED;
					}
					break;
				}

				lineBuilder.append(color);
				if (fieldValue != null) {
					lineBuilder.append(
					        fieldSize >= 0 ? fieldValue : fieldValue.substring(0, fieldValue.length() + fieldSize));
				}
				lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
				lineBuilder.append('|');
			}
			lineBuilder.append(System.lineSeparator());
		}
		lineBuilder.append(AnsiConstants.FOREGROUNG_GRAY);
		System.out.println(lineBuilder.toString());
	}

	@Override
	public void updateStatistic(final WatcherStatistics statistic) {
		addConfiguration(watcherStats, statistic);
		updateStatistics();
	}

	@Override
	public void updateStatistic(final TransactionStatistics statistic) {
		addConfiguration(txStats, statistic);
		updateStatistics();
	}

	private <T extends IStatistics> void addConfiguration(final List<T> list, final T stats) {
		if (stats == null) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getConfigurationId() == stats.getConfigurationId()) {
				list.set(i, stats);
				return;
			}
		}
		list.add(stats);
	}

}
