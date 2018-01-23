package pl.projewski.bitcoin.store.json;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

@Data
class StoreContainer {
	private List<WatcherConfig> watchList = new ArrayList<>();
	private List<TransactionConfig> transactionList = new ArrayList<>();
}
