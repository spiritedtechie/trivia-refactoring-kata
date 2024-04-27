package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
	ArrayList players = new ArrayList();
	int[] places = new int[6];
	int[] purses = new int[6];
	boolean[] inPenaltyBox = new boolean[6];

	LinkedList popQuestions = new LinkedList();
	LinkedList scienceQuestions = new LinkedList();
	LinkedList sportsQuestions = new LinkedList();
	LinkedList rockQuestions = new LinkedList();

	int currentPlayer = 0;
	boolean isGettingOutOfPenaltyBox;

	public Game() {
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
		}
	}

	public String createRockQuestion(int index) {
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
		if (players.size() == 5) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		players.add(playerName);
		places[howManyPlayers()] = 0;
		purses[howManyPlayers()] = 0;
		inPenaltyBox[howManyPlayers()] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + players.size());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				updateNextPlace(currentPlayer, roll);
				System.out.println("The category is " + currentCategory(places[currentPlayer]));
				askQuestion();
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
			}

		} else {

			updateNextPlace(currentPlayer, roll);
			System.out.println("The category is " + currentCategory(places[currentPlayer]));
			askQuestion();
		}

	}

	void updateNextPlace(int playerNumber, int roll) {
		places[playerNumber] = places[playerNumber] + roll;
		if (places[playerNumber] > 11)
			places[playerNumber] = places[playerNumber] - 12;

		System.out.println(players.get(playerNumber)
				+ "'s new location is "
				+ places[playerNumber]);
	}

	private void askQuestion() {
		String question = (String) getQuestion(currentCategory(places[currentPlayer]));
		System.out.println(question);
	}

	Object getQuestion(String category) {
		LinkedList questionsForCategory = null;
		if (category == "Pop")
			questionsForCategory = popQuestions;
		if (category == "Science")
			questionsForCategory = scienceQuestions;
		if (category == "Sports")
			questionsForCategory = sportsQuestions;
		if (category == "Rock")
			questionsForCategory = rockQuestions;

		if (questionsForCategory == null) {
			return null;
		}

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.removeFirst();
	}

	String currentCategory(int playerPlace) {
		switch (playerPlace) {
			case 0:
			case 4:
			case 8:
				return "Pop";
			case 1:
			case 5:
			case 9:
				return "Science";
			case 2:
			case 6:
			case 10:
				return "Sports";
			default:
				return "Rock";
		}
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]) {
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");

				boolean winner = didPlayerWin();
				goToNextPlayer();

				return winner;
			} else {
				goToNextPlayer();
				return true;
			}

		} else {

			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");

			boolean winner = didPlayerWin();
			goToNextPlayer();

			return winner;
		}
	}

	void goToNextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size())
			currentPlayer = 0;
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;

		goToNextPlayer();
		return true;
	}

	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
