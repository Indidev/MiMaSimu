package org.loadingScreen;

public class ColorCalculator implements Runnable {

	private double x;
	private static final double STEP = Math.PI / 16;
	private LoadingScreen screen;
	private long timeout;
	private boolean stopped;

	public ColorCalculator(LoadingScreen screen, long timeout) {
		x = 0;
		this.screen = screen;
		this.timeout = timeout;
	}

	public void stop() {
		this.stopped = true;
	}

	@Override
	public void run() {
		stopped = false;
		while (!stopped) {
			float r = Math.abs((float) (Math.cos(x / 2)));
			float g = Math.abs((float) (Math.cos(x / 5)));
			float b = Math.abs((float) (Math.cos(x / 3)));

			if (r > 1)
				r = 1;
			if (g > 1)
				g = 1;
			if (b > 1)
				b = 1;
			if (r < 0)
				r = 0;
			if (g < 0)
				g = 0;
			if (b < 0)
				b = 0;

			screen.setColor(r, g, b);

			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}

			x += STEP;
		}
	}
}
