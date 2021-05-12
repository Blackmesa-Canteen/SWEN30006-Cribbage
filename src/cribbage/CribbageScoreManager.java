package cribbage;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

/**
 * @author Xiaotian
 * @program Cribbage
 * @description
 * @create 2021-05-11 11:51
 */
public class CribbageScoreManager implements CardGameScoreManager {

    CardGame cardGame;
    LogHandler logHandler;

    private final Location[] scoreLocations = {
            new Location(590, 25),
            new Location(590, 675)
    };
    private final Actor[] scoreActors = {null, null};
    final Font normalFont = new Font("Serif", Font.BOLD, 24);
    final Font bigFont = new Font("Serif", Font.BOLD, 36);

    private final int[] scores;
    private final int nPlayers;

    public CribbageScoreManager(int nPlayers, CardGame cardGame) {
        this.scores = new int[nPlayers];
        this.nPlayers = nPlayers;
        this.cardGame = cardGame;
    }

    /**
     * init scores to 0 and init text score board
     */
    @Override
    public void initScores() {
        for (int i = 0; i < nPlayers; i++) {
            scores[i] = 0;
            scoreActors[i] = new TextActor("0", Color.WHITE, cardGame.bgColor, bigFont);
            cardGame.addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    @Override
    public void updateScoreActorOfPlayer(int player) {

        if(player < nPlayers) {
            cardGame.removeActor(scoreActors[player]);

            // get player score from scoreManager's score Array
            int playerScore = scores[player];
            scoreActors[player] = new TextActor(String.valueOf(playerScore), Color.WHITE, cardGame.bgColor, bigFont);
            cardGame.addActor(scoreActors[player], scoreLocations[player]);
        } else {
            System.out.println("player number err in updateScoreActorOfPlayer");
        }
    }

    @Override
    public int[] getScoreArray() {
        return scores;
    }

    /**
     * Set a new score to a specific player, then update the actor
     * @param newScore int score
     * @param player player id, id is less than nPlayers
     */
    public void setScore(int newScore, int player) {
        if(player < nPlayers) {
            scores[player] = newScore;
            updateScoreActorOfPlayer(player);
        } else {
            System.out.println("player number err in setScore");
        }
    }

    /**
     * Add a new delta score to the player's score, then update the actor
     * @param deltaScore new incoming score that needs to be added
     * @param player player id
     * @param reason reason why we add the score to the player
     */
    @Override
    public void addScoreToPlayer(int deltaScore, int player, String reason) {
        if(logHandler == null) {
            System.out.println("logHandler for ScoreManager is NULL");
            return;
        }
        if(player < nPlayers) {
            scores[player] += deltaScore;
            updateScoreActorOfPlayer(player);
            logHandler.writeMessageToLog("score,P" +
                    player +
                    "," +
                    scores[player] +
                    "," +
                    deltaScore +
                    "," +
                    reason);
        } else {
            System.out.println("player number err in addScoreToPlayer");
        }
    }

    @Override
    public void minusScoreToPlayer(int deltaScore, int player, String reason) {
        // no need to minus scores for cribbage game
    }

    /**
     * Get a player's score by his ID
     * @param player player id
     * @return score
     */
    @Override
    public int getScore(int player) {
        if(player < nPlayers) {
            return scores[player];
        } else {
            System.out.println("player number err in getScore");
        }

        return 0;
    }

    @Override
    public void attachLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }
}