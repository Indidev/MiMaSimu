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
				try {
					Thread.sleep(timeout / 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sw.clockOff();

				try {
					Thread.sleep(timeout / 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
}
