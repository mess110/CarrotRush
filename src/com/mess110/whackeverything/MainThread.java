package com.mess110.whackeverything;

import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private static final String TAG = MainThread.class.getSimpleName();

	private SurfaceHolder surfaceHolder;
	private MainGamePanel gamePanel;
	private boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		long tickCount = 0L;
		Log.d(TAG, "Starting game loop");
		while (running) {
			tickCount++;
			// update game state
			// render state to the screen
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
	}
}
