
package com.adaptionsoft.games.trivia.runner;

import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;

public class BetterGameRunner {

	public static void main(String[] args) {
		Game game = new Game();

		game.add("Chet");
		game.add("Pat");
		game.add("Sue");

		Random rand = new Random();

		boolean isWinner;
		do {
			int diceRollResult = rand.nextInt(5) + 1;
			game.rollDice(diceRollResult);

			if (rand.nextInt(9) == 7) {
				isWinner = game.handleWrongAnswer();
			} else {
				isWinner = game.handleCorrectAnswer();
			}
		} while (!isWinner);

	}
}
