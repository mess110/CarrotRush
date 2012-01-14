package com.mess110.carrotrush.model;

import java.util.Random;

public class Util {

	public static int generateNumber() {
		Random random = new Random();
		return random.nextInt(3) + 1;
	}

	public static String format(long totalPoints) {
		return String.valueOf(totalPoints);
	}
}
