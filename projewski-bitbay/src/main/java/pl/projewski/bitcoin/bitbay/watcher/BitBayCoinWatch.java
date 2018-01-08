package pl.projewski.bitcoin.bitbay.watcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import pl.projewski.bitcoin.bitbay.api.v2.Trade;
import pl.projewski.bitcoin.common.TransactionConfig;
import pl.projewski.bitcoin.common.WatcherConfig;

@Data
public class BitBayCoinWatch {
	private LinkedList<Trade> trades = new LinkedList<>();
	private WatcherConfig config;
	private List<TransactionConfig> transactions = new ArrayList<>();
}
