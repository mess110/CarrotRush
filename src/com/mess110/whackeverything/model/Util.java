package com.mess110.whackeverything.model;

import java.util.Random;

public class Util {

	public static int generateNumber() {
		Random random = new Random();
		return random.nextInt(3) + 1;
	}
}
