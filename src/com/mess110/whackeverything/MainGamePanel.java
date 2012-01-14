package com.mess110.whackeverything;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mess110.whackeverything.model.AnimatedSprite;
import com.mess110.whackeverything.model.Sprite;
import com.mess110.whackeverything.model.Util;

// every time player makes a mistake bunny takes a shit
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

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

	private Sprite bubble;

	private Paint gameText;
	private long totalPoints = 0;
	private long totalTime = 0;
	private long bonusCycle = 2;
	private long prevTime = System.currentTimeMillis();

	public MainGamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);

		bunny = new AnimatedSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.bunny), -30, 200, 2, 10);

		trees = new ArrayList<Sprite>();

		grassPaint = new Paint();
		grassPaint.setColor(Color.parseColor("#68cd00"));

		gameText = new Paint();
		gameText.setColor(Color.BLACK);
		gameText.setFakeBoldText(false);
		gameText.setTextSize(16);

		sun = new AnimatedSprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.sun6), 10, 10, 2, 2);

		bubble = new Sprite(BitmapFactory.decodeResource(getResources(),
				R.drawable.points2), 50, 50);

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
			// delegating event handling to the droid
			// for (AnimatedSprite bunny : bunnyCollection.getBunnies()) {
			// bunny.handleActionDown(event);
			// }
			if (event.getX() < width / 3 && event.getY() > height - height / 3) {
				if (correctButton == 1) {
					speed += 1;
				} else {
					speed = 0;
				}
			}

			if (width / 3 < event.getX() && event.getX() < width / 3 * 2
					&& event.getY() > height - height / 3) {
				if (correctButton == 2) {
					speed += 1;
				} else {
					speed = 0;
				}
			}

			if (width / 3 * 2 < event.getX()
					&& event.getY() > height - height / 3) {
				if (correctButton == 3) {
					speed += 1;
				} else {
					speed = 0;
				}
			}

			correctButton = Util.generateNumber();

			// check if in the lower part of the screen we exit
			if (event.getY() < 64 && event.getX() > getWidth() - 100) {
				// thread.setRunning(false);
				// ((Activity) getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!loaded) {
			canvas.drawColor(Color.parseColor("#1fb6f6"));
		} else {
			// draw background
			canvas.drawColor(Color.parseColor("#1fb6f6"));
			canvas.drawRect(-800, height / 3, 800, 500, grassPaint);

			sun.setX(width / 2 - sun.getSpriteWidth() / 2);
			sun.draw(canvas);

			for (Sprite tree : trees) {
				tree.draw(canvas);
			}

			bunny.draw(canvas);
			bubble.draw(canvas);
			int bubbleX = bubble.getX() - bubble.getX() / 10;
			int bubbleY = bubble.getY() + bubble.getBitmap().getHeight() / 10;
			canvas.drawText(String.valueOf(speed), bubbleX, bubbleY, gameText);

			canvas.drawText(String.valueOf(totalPoints), bubbleX + 30, bubbleY,
					gameText);

			switch (correctButton) {
			case 1:
				//
				left.draw(canvas);
				break;
			case 2:
				//
				middle.draw(canvas);
				break;
			case 3:
				//
				right.draw(canvas);
				break;
			default:
				break;
			}
		}
	}

	public void update() {
		if (!loaded) {
			width = getWidth();
			height = getHeight();

			for (int i = 1; i <= 5; i++) {
				trees.add(new Sprite(BitmapFactory.decodeResource(
						getResources(), R.drawable.tree), width / 4 * i - 90,
						height / 3));
			}

			left = new Sprite(BitmapFactory.decodeResource(getResources(),
					R.drawable.carrot), width / 3 - width / 6, height / 3 * 2
					+ height / 6);
			middle = new Sprite(BitmapFactory.decodeResource(getResources(),
					R.drawable.carrot), width / 2, height / 3 * 2 + height / 6);
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

		long current = System.currentTimeMillis();
		totalTime += current - prevTime;
		if (bonusCycle > 500) {
			bonusCycle = 0;
			totalPoints += speed;
			if (speed > 0) {
				speed--;
			}
		} else {
			bonusCycle += current - prevTime;
		}
		prevTime = current;
	}
}
