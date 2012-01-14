package com.mess110.whackeverything.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimatedSprite extends Sprite {

	private int currentFrame;
	private int frameNr;
	private int spriteWidth;
	private int spriteHeight;
	private Rect sourceRect;
	private int framePeriod;
	private long frameTicker;

	public AnimatedSprite(Bitmap bitmap, int x, int y, int frameCount, int fps) {
		super(bitmap, x, y);
		currentFrame = 0;
		frameNr = frameCount;
		spriteWidth = bitmap.getWidth() / frameCount;
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
	}

	public int getFrameCount() {
		return frameNr;
	}

	public void update(long gameTime) {
		if (gameTime > frameTicker + framePeriod) {
			frameTicker = gameTime;
			currentFrame++;
			if (currentFrame >= frameNr) {
				currentFrame = 0;
			}
		}
		this.sourceRect.left = currentFrame * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}

	@Override
	public void draw(Canvas canvas) {
		Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY()
				+ spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void resetPosition(int width) {
		if (x > width) {
			x = (-spriteWidth / frameNr);
		}
	}

}
