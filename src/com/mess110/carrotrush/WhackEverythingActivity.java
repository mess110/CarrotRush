package com.mess110.carrotrush;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class WhackEverythingActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = WhackEverythingActivity.class
			.getSimpleName();
	private MainGamePanel mainGamePanel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// System.runFinalizersOnExit(true);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mainGamePanel = new MainGamePanel(this);
		setContentView(mainGamePanel);
		Log.d(TAG, "View added");
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		mainGamePanel.finish();
		return;
	}
}