package pl.projewski.bitcoin.portfolio;

import java.util.TimerTask;

public class OneTask extends TimerTask {
	private final Runnable runnable;

	public OneTask(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}
}
