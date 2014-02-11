package fr.gh.spacecontrol.logic;

import java.util.Random;

public class MathTool {

	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static float speedCalculator(int startPosX, int startPosY, int finishPosX, int finishPosY, float time) {

		// calculate speed by getting distance over time

		int adjacent = finishPosX - startPosX;
		int opposed = finishPosY - startPosY;
		int distance = (int) Math.sqrt(Math.pow(adjacent, 2) + Math.pow(opposed, 2));

		float speed = distance / time;
		return speed;
	}
}
