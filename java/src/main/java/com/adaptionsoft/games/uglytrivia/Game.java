package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Player {
	private String name;
	private int place = 0;
	private int purse = 0;

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPurse() {
		return purse;
	}

	public void addToPurse(int coins) {
		this.purse += coins;
	}
}

public class Game {
	List<Player> players = new ArrayList<>();
	boolean[] inPenaltyBox = new boolean[6];

	private Map<String, LinkedList<String>> questions = new HashMap<>();

	int currentPlayer = 0;
	boolean isGettingOutOfPenaltyBox;

	public Game() {
		LinkedList<String> popQuestions = new LinkedList<String>();
		LinkedList<String> scienceQuestions = new LinkedList<String>();
		LinkedList<String> sportsQuestions = new LinkedList<String>();
		LinkedList<String> rockQuestions = new LinkedList<String>();

		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast("Rock Question " + i);
		}

		questions.put("Pop", popQuestions);
		questions.put("Science", scienceQuestions);
		questions.put("Sports", sportsQuestions);
		questions.put("Rock", rockQuestions);
	}

	public boolean isPlayable() {
		return (numberOfPlayers() >= 2);
	}

	public boolean add(String playerName) {
		if (numberOfPlayers() == 5) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		Player player = new Player(playerName);
		players.add(player);

		inPenaltyBox[numberOfPlayers()] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + numberOfPlayers());
		return true;
	}

	public int numberOfPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(getPlayerName(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox(currentPlayer)) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				updateNextPlace(currentPlayer, roll);
				askQuestion();
			} else {
				System.out.println(getPlayerName(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
			}

		} else {

			updateNextPlace(currentPlayer, roll);
			askQuestion();
		}

	}



	Object getPlayerName(int playerNumber) {
		if (playerNumber >= numberOfPlayers() || playerNumber < 0) {
			throw new IllegalArgumentException("Invalid player number");
		}

		return players.get(playerNumber).getName();
	}

	void updateNextPlace(int playerNumber, int roll) {
		Player player = players.get(playerNumber);
		Integer newPlace = player.getPlace() + roll;
		if (newPlace > 11)
			newPlace = newPlace - 12;
		player.setPlace(newPlace);

		System.out.println(getPlayerName(playerNumber)
				+ "'s new location is "
				+ player.getPlace());
	}

	private void askQuestion() {
		String category = getCategory(players.get(currentPlayer).getPlace());
		System.out.println("The category is " + getCategory(players.get(currentPlayer).getPlace()));
		String question = (String) getNextQuestion(category);
		System.out.println(question);
	}

	Object getNextQuestion(String category) {
		LinkedList<String> questionsForCategory = questions.get(category);

		if (questionsForCategory == null) {
			return null;
		}

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.removeFirst();
	}

	String getCategory(int playerPlace) {
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
		if (inPenaltyBox(currentPlayer)) {
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				addCoinToPurse(currentPlayer);

				boolean notWinner = !didPlayerWin(currentPlayer);
				goToNextPlayer();

				return notWinner;
			} else {
				goToNextPlayer();
				return true;
			}

		} else {

			System.out.println("Answer was corrent!!!!");
			addCoinToPurse(currentPlayer);

			boolean notWinner = !didPlayerWin(currentPlayer);
			goToNextPlayer();

			return notWinner;
		}
	}

	void addCoinToPurse(int playerNumber) {
		if (playerNumber >= numberOfPlayers() || playerNumber < 0) {
			throw new IllegalArgumentException("Invalid player number");
		}
		Player player = players.get(playerNumber);
		player.addToPurse(1);
		System.out.println(getPlayerName(playerNumber)
				+ " now has "
				+ player.getPurse()
				+ " Gold Coins.");
	}

	void goToNextPlayer() {
		currentPlayer++;
		if (currentPlayer == numberOfPlayers())
			currentPlayer = 0;
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(getPlayerName(currentPlayer) + " was sent to the penalty box");
		putInPenaltyBox(currentPlayer);

		goToNextPlayer();
		return true;
	}

	boolean didPlayerWin(int playerNumber) {
		Player player = players.get(playerNumber);
		return player.getPurse() == 6;
	}

	boolean inPenaltyBox(int playerNo) {
		return inPenaltyBox[playerNo];
	}

    void putInPenaltyBox(int playerNumber) {
        inPenaltyBox[playerNumber] = true;
    }
}
