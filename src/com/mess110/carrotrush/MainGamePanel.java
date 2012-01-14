package com.mess110.carrotrush;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mess110.carrotrush.model.AnimatedSprite;
import com.mess110.carrotrush.model.Sprite;
import com.mess110.carrotrush.model.Util;

public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private static final long DEFAULT_BONUS_CYCLE = 500;
	private static final int MAX_STARS = 12;
	private static final long CORRECT_BUTTON_POINTS = 7;
	private static final long GAME_LENGTH = 60000;
	private static final long SCORE_LENGTH = 5000;

	private MainThread thread;

	private AnimatedSprite sun;
	private int height, width;
	private Sprite left, middle, right;

	private boolean loaded = false;

	private int speed = 0;
	private Paint grassPaint;
	private ArrayList<Sprite> trees;

	private int correctButton = 2;

	private AnimatedSprite bunny;

	private Paint gameText;
	private long totalPoints = 0;
	private long totalTime = 0;
	private long bonusCycle = 2;
	private long prevTime = System.currentTimeMillis();

	private ArrayList<Sprite> stars;

	private boolean showScore;

	public MainGamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);

		bunny = new AnimatedSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.bunny), -30, 200, 2, 10);

		trees = new ArrayList<Sprite>();
		stars = new ArrayList<Sprite>();
		for (int i = 0; i <= MAX_STARS - 1; i++) {
			stars.add(new Sprite(BitmapFactory.decodeResource(getResources(),
					R.drawable.star), 50, 50));
		}

		grassPaint = new Paint();
		grassPaint.setColor(Color.parseColor("#68cd00"));

		gameText = new Paint();
		gameText.setColor(Color.WHITE);
		gameText.setFakeBoldText(true);
		gameText.setTextSize(16);
		gameText.setAntiAlias(true);

		sun = new AnimatedSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.sun6), 10, 10, 2, 2);

		thread = new MainThread(getHolder(), this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!showScore) {
				if (event.getX() < width / 3
						&& event.getY() > height - height / 3) {
					handleButtonPress(correctButton, 1);
				} else if (width / 3 < event.getX()
						&& event.getX() < width / 3 * 2
						&& event.getY() > height - height / 3) {
					handleButtonPress(correctButton, 2);
				} else if (width / 3 * 2 < event.getX()
						&& event.getY() > height - height / 3) {
					handleButtonPress(correctButton, 3);
				}

				// check if in the lower part of the screen we exit
				if (event.getY() < 64 && event.getX() > getWidth() - 64) {
					finish();
				} else {
					Log.d(TAG,
							"Coords: x=" + event.getX() + ",y=" + event.getY());
				}
			} else {
				// finish();
			}
		}
		return true;
	}

	private void handleButtonPress(int correctBtn, int pressed) {
		if (correctBtn == pressed) {
			if (speed < MAX_STARS) {
				speed += 1;
				totalPoints += CORRECT_BUTTON_POINTS;
			}
		} else {
			if (speed > 0) {
				speed -= 1;
			}
		}
		correctButton = Util.generateNumber();
	}

	public void finish() {
		thread.setRunning(false);
		((Activity) getContext()).finish();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#1fb6f6"));
		if (loaded) {
			if (!showScore) {
				canvas.drawRect(-800, height / 3, 800, 500, grassPaint);
				sun.draw(canvas);

				for (Sprite tree : trees) {
					tree.draw(canvas);
				}

				for (int i = 0; i < MAX_STARS; i++) {
					if (speed > i)
						stars.get(i).draw(canvas);
				}

				bunny.draw(canvas);
				canvas.drawText(Util.format(totalPoints), 20, trees.get(0)
						.getY() - trees.get(0).getBitmap().getHeight() / 3,
						gameText);

				switch (correctButton) {
				case 1:
					left.draw(canvas);
					break;
				case 2:
					middle.draw(canvas);
					break;
				case 3:
					right.draw(canvas);
					break;
				default:
					break;
				}
			} else {
				gameText.setTextSize(64);
				canvas.drawText(Util.format(totalPoints), width / 2,
						height / 2, gameText);
			}

			if (totalTime > GAME_LENGTH && showScore == false) {
				showScore = true;
			}
			if (totalTime > GAME_LENGTH + SCORE_LENGTH) {
				finish();
			}
		}
	}

	public void update() {
		if (!showScore) {
			if (!loaded) {
				width = getWidth();
				height = getHeight();

				sun.setX(width - sun.getSpriteWidth());

				for (int i = 1; i <= 5; i++) {
					trees.add(new Sprite(BitmapFactory.decodeResource(
							getResources(), R.drawable.tree), width / 4 * i
							- 90, height / 3));
				}

				int c = 1;
				for (Sprite star : stars) {
					star.setX(star.getBitmap().getWidth() * c / 2);
					star.setY(star.getBitmap().getHeight() / 2 + 1);
					c++;
				}

				left = new Sprite(BitmapFactory.decodeResource(getResources(),
						R.drawable.carrot), width / 3 - width / 6, height / 3
						* 2 + height / 6);
				middle = new Sprite(BitmapFactory.decodeResource(
						getResources(), R.drawable.carrot), width / 2, height
						/ 3 * 2 + height / 6);
				right = new Sprite(BitmapFactory.decodeResource(getResources(),
						R.drawable.carrot), width - width / 6, height / 3 * 2
						+ height / 6);

				bunny.setY(height / 3 + height / 6);
				loaded = !loaded;
			} else {
				bunny.resetPosition(width);

				bunny.setX(bunny.getX() + speed);

				bunny.update(System.currentTimeMillis());
				sun.update(System.currentTimeMillis());
			}
		}

		long current = System.currentTimeMillis();
		totalTime += current - prevTime;
		if (!showScore) {
			if (bonusCycle > DEFAULT_BONUS_CYCLE) {
				bonusCycle = 0;
				totalPoints += speed;
				if (speed > 0) {
					speed--;
				}
			} else {
				bonusCycle += current - prevTime;
			}
		}
		prevTime = current;
	}
}
