package pl.projewski.bitcoin.ui.api;

public interface IUserInterface {

	void start(String[] args);

	IStatisticsDrawer getStatisticDrawer();
}
