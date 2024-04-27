package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameTest {

	private Game game = new Game();

	@Test
	public void test_thatCurrentPlayerPositionCannotExceedNumberOfPlayers() throws Exception {
		game.add("Bob");
		game.add("John");

		assertEquals(0, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(1, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(0, game.currentPlayer);
	}

	@Test
	public void test_thatThereCanBeAtMost5Players() {
		game.add("Bob");
		game.add("John");
		game.add("George");
		game.add("Fred");
		game.add("Jane");

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			game.add("Jane");
		});

		String expectedMessage = "Can only add upto and including 5 players";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void test_thatAPlayersNextPlaceInTurnsIsCurrentPlacePlusTheRollValue() {
		game.add("Bob");
		game.add("John");

		game.updateNextPlace(0, 2);

		assertEquals(2, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceInTurnsDoesntExceed11() {
		game.add("Bob");
		game.add("John");

		game.updateNextPlace(0, 12);

		assertEquals(0, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceInTurnsMaxesAt11() {
		game.add("Bob");
		game.add("John");

		game.updateNextPlace(0, 11);

		assertEquals(11, game.places[0]);
	}
}
