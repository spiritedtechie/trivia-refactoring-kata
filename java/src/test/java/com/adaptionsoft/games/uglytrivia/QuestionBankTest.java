package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuestionBankTest {

	private QuestionBank questionBank = new QuestionBank();

	@Test
	public void test_getNextQuestionForEachCategory() {
		assertEquals("Pop Question 0", questionBank.getNextQuestion(Category.Pop));
		assertEquals("Pop Question 1", questionBank.getNextQuestion(Category.Pop));

		assertEquals("Science Question 0", questionBank.getNextQuestion(Category.Science));
		assertEquals("Science Question 1", questionBank.getNextQuestion(Category.Science));

		assertEquals("Sports Question 0", questionBank.getNextQuestion(Category.Sports));
		assertEquals("Sports Question 1", questionBank.getNextQuestion(Category.Sports));

		assertEquals("Rock Question 0", questionBank.getNextQuestion(Category.Rock));
		assertEquals("Rock Question 1", questionBank.getNextQuestion(Category.Rock));
	}

	@Test
	public void test_getNextQuestionErrorsOnceQuestionsForACategoryHaveRanOut() {
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			for (int r = 0; r <= 50; r++) {
				questionBank.getNextQuestion(Category.Pop);
			}
		});

		assertTrue(exception.getMessage().contains("No further questions for category"));
	}
}
