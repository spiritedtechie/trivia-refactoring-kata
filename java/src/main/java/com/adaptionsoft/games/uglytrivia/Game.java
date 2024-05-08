package com.adaptionsoft.games.uglytrivia;

import static com.adaptionsoft.games.uglytrivia.QuestionBank.getCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

class Player {
	private static final int WINNING_PURSE_AMOUNT = 6;
	private static final int TOTAL_PLACES = 12;

	private final String name;
	private final int place;
	private final int purse;
	private final boolean inPenaltyBox;

	public Player(String name) {
		this(name, 0, 0, false);
	}

	public Player(String name, int place, int purse, boolean inPenaltyBox) {
		this.name = name;
		this.place = place;
		this.purse = purse;
		this.inPenaltyBox = inPenaltyBox;
	}

	String getName() {
		return name;
	}

	int getPlace() {
		return place;
	}

	int getPurse() {
		return purse;
	}

	Player addToPurse(int coins) {
		return new Player(this.name, this.place, this.purse + coins, this.inPenaltyBox);
	}

	boolean isInPenaltyBox() {
		return inPenaltyBox;
	}

	Player setInPenaltyBox(boolean inPenaltyBox) {
		return new Player(this.name, this.place, this.purse, inPenaltyBox);
	}

	Player updateNextPlace(int roll) {
		return new Player(this.name, (this.place + roll) % TOTAL_PLACES, this.purse, this.inPenaltyBox);
	}

	boolean didPlayerWin() {
		return getPurse() == WINNING_PURSE_AMOUNT;
	}
}

class QuestionBank {
	enum Category {
		Pop, Science, Sports, Rock
	}

	private static final int NUMBER_OF_QUESTIONS_PER_CATEGORY = 50;

	private final EnumMap<Category, List<String>> questions = new EnumMap<>(Category.class);

	public QuestionBank() {
		initialiseQuestions();
	}

	private void initialiseQuestions() {
		for (Category category : Category.values()) {
			List<String> categoryQuestions = new LinkedList<String>();
			for (int i = 0; i < NUMBER_OF_QUESTIONS_PER_CATEGORY; i++) {
				categoryQuestions.add(category.name() + " Question " + i);
			}
			questions.put(category, categoryQuestions);
		}
	}

	String getNextQuestion(Category category) {
		List<String> questionsForCategory = questions.get(category);

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.remove(0);
	}

	static Category getCategory(int playerPlace) {
		switch (playerPlace) {
			case 0:
			case 4:
			case 8:
				return Category.Pop;
			case 1:
			case 5:
			case 9:
				return Category.Science;
			case 2:
			case 6:
			case 10:
				return Category.Sports;
			default:
				return Category.Rock;
		}
	}

}

public class Game {

	private static final int MAXIMUM_NUMBER_OF_PLAYERS_ALLOWED = 5;

	private final List<Player> players = new ArrayList<>();

	private final QuestionBank questionBank;

	private int currentPlayerIndex = 0;

	public Game() {
		questionBank = new QuestionBank();

	}

	public boolean add(String playerName) {
		if (numberOfPlayers() == MAXIMUM_NUMBER_OF_PLAYERS_ALLOWED) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		players.add(new Player(playerName));

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + numberOfPlayers());
		return true;
	}

	/**
	 * @deprecated use rollDice instead
	 */
	@Deprecated
	public void roll(int roll) {
		this.rollDice(roll);
	}

	/**
	 * 
	 * @param roll
	 */
	public void rollDice(int roll) {
		if (roll <= 0 || roll > 5) {
			throw new IllegalArgumentException("Dice roll should be between 1 and 5 inclusive");
		}

		Player player = getCurrentPlayer();

		System.out.println(player.getName() + " is the current player");
		System.out.println("They have rolled a " + roll);

		boolean isEvenRoll = roll % 2 == 0;

		if (player.isInPenaltyBox() && isEvenRoll) {
			System.out.println(player.getName() + " is not getting out of the penalty box");
		} else {
			player = player.setInPenaltyBox(false).updateNextPlace(roll);
			this.setPlayerNumbered(currentPlayerIndex, player);

			System.out.println(player.getName() + "'s new location is " + player.getPlace());
			askQuestion();
		}

	}

	/**
	 * @deprecated use handleCorrectAnswer instead, which is an interface breaking
	 *             change as the returned boolean is reversed (see javadoc for
	 *             return values)
	 * @return returns whether the play did NOT win.
	 */
	@Deprecated
	public boolean wasCorrectlyAnswered() {
		return !this.handleCorrectAnswer();
	}

	/**
	 * Handler for current player answering question correctly after roll
	 * 
	 * @return returns whether the play DID win.
	 */
	public boolean handleCorrectAnswer() {
		Player player = getCurrentPlayer();
		if (player.isInPenaltyBox()) {
			goToNextPlayer();
			return false;
		}

		player = player.addToPurse(1);
		boolean isWinner = player.didPlayerWin();
		this.setPlayerNumbered(currentPlayerIndex, player);

		goToNextPlayer();

		System.out.println("Answer was correct!!!!");
		System.out.println(player.getName() + " now has " + player.getPurse() + " Gold Coins.");

		return isWinner;
	}

	/**
	 * @deprecated use handleWrongeAnswer instead
	 * @return returns a fixed value of True, negating the return from
	 *         handleWrongAnswer
	 */
	@Deprecated
	public boolean wrongAnswer() {
		return !this.handleWrongAnswer();
	}

	/**
	 * Handler for current player answering question incorrectly after roll
	 * 
	 * @return returns a fixed value of False for the play did win.
	 */
	public boolean handleWrongAnswer() {
		Player player = getCurrentPlayer();
		player = player.setInPenaltyBox(true);
		this.setPlayerNumbered(currentPlayerIndex, player);

		goToNextPlayer();

		System.out.println("Question was incorrectly answered");
		System.out.println(player.getName() + " was sent to the penalty box");

		return false;
	}

	void askQuestion() {
		QuestionBank.Category category = getCategory(getCurrentPlayer().getPlace());
		System.out.println("The category is " + category);
		String question = (String) questionBank.getNextQuestion(category);
		System.out.println(question);
	}

	int numberOfPlayers() {
		return players.size();
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	Player getPlayerNumbered(int playerNo) {
		return players.get(playerNo);
	}

	void setPlayerNumbered(int playerNo, Player player) {
		players.set(playerNo, player);
	}

	void goToNextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers();
	}
}
