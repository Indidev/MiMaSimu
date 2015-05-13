package org.components;

public class Clock implements Runnable {
	private int timeout;
	private SW sw;
	private boolean paused;
	private boolean stopped;

	public Clock(int timeout, SW sw) {
		this.timeout = timeout;
		this.sw = sw;
		this.stopped = true;
		this.paused = false;
	}

	@Override
	public void run() {
		stopped = false;
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (!stopped) {
			if (!paused) {
				sw.clock();
				sleep(timeout / 2);
				sw.clockOff();
				sleep(timeout / 2);
			} else
				sleep(100);
		}
	}

	public void pause(boolean value) {
		this.paused = value;
	}

	public void stop() {
		this.stopped = true;
	}

	public void setSW(SW sw) {
		this.sw = sw;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout + 10;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
