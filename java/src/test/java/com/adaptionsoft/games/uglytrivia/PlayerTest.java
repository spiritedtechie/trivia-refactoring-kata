package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player = new Player("Bob");

    @Test
    public void test_getPlayerName() {
        Player player2 = new Player("John");
        assertEquals("Bob", player.getName());
        assertEquals("John", player2.getName());
    }

    @Test
    public void test_updateNextPlaceIsCurrentPlacePlusTheRollValue() {
        assertEquals(0, player.getPlace());

        Player updated = player.updateNextPlace(2);

        assertEquals(2, updated.getPlace());

    }

    @Test
    public void test_updateNextPlaceCannotExceed11AndCirclesBackRound() {
        player.updateNextPlace(12);

        assertEquals(0, player.getPlace());
    }

    @Test
    public void test_updateNextPlaceMaxesAt11() {
        Player updated = player.updateNextPlace(11);

        assertEquals(11, updated.getPlace());
    }

    @Test
    public void test_add1CoinToPurseForPlayer() {
        assertEquals(0, player.getPurse());

        Player updated = player.addToPurse(1);

        assertEquals(1, updated.getPurse());
    }

    @Test
    public void test_add2CoinToPurseForPlayer() {
        assertEquals(0, player.getPurse());

        Player updated = player.addToPurse(1);
        updated = updated.addToPurse(1);

        assertEquals(2, updated.getPurse());
    }

    @Test
    public void test_didPlayerWin_False_whenPurseLessThan6() {
        addCoinsToPurse(player, 5);

        assertFalse(player.didPlayerWin());
    }

    @Test
    public void test_didPlayerWin_false_whenPurseGreaterThan6() {
        addCoinsToPurse(player, 7);

        assertFalse(player.didPlayerWin());
    }

    @Test
    public void test_didPlayerWin_true_whenPurseEqualToSix() {
        Player updated = addCoinsToPurse(player, 6);

        assertTrue(updated.didPlayerWin());
    }

    private Player addCoinsToPurse(Player player, int coinCount) {
        Player updated = player;
        for (int times = 1; times <= coinCount; times++) {
            updated = updated.addToPurse(1);
        }
        return updated;
    }

    @Test
    public void test_inPenaltyBox_false() {
        assertFalse(player.isInPenaltyBox());
    }

    @Test
    public void test_inPenaltyBox_true() {
        assertFalse(player.isInPenaltyBox());

        Player updated = player.setInPenaltyBox(true);

        assertTrue(updated.isInPenaltyBox());
    }
}
