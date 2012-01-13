package com.mess110.whackeverything;

import android.graphics.Bitmap;
import android.util.Log;

public class Droid extends Sprite{

	private boolean touched; // if droid is touched/picked up
	private Speed speed;

	public Droid(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
		this.speed = new Speed();
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}	

	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (x - bitmap.getWidth() / 2)
				&& (eventX <= (x + bitmap.getWidth() / 2))) {
			if (eventY >= (y - bitmap.getHeight() / 2)
					&& (y <= (y + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
				Log.d("Droid", "touched");
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}

	}
	
	public void update() {
		x += (speed.getXv() * speed.getxDirection());
		//y += (speed.getYv() * speed.getyDirection());
	}

	public Speed getSpeed() {
		return speed;
	}

}
