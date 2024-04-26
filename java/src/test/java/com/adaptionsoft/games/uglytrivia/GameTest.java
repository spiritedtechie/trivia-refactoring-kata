package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class GameTest {

	private Game game = new Game();

	@Test
	public void test_thatCurrentPlayerCannotExceedNumberOfPlayers() throws Exception {

		game.add("Bob");
		game.add("John");	
		
		assertEquals(0, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(1, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(0, game.currentPlayer);
	}
}
