package pl.projewski.bitcoin.bitbay.watcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import pl.projewski.bitcoin.bitbay.api.v2.Trade;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

@Data
public class BitBayCoinWatch {
	private LinkedList<Trade> trades = new LinkedList<>();
	private WatcherConfig config;
	private List<TransactionConfig> transactions = new ArrayList<>();
	private WatcherStatistics lastStatistics;
}
