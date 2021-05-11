package cribbage;

import ch.aplu.jcardgame.CardGame;

import java.awt.*;

public interface CardGameScoreManager {

    public void initScores();
    public void updateScoreActorOfPlayer(int player);
    public int[] getScoreArray();
    public void setScore(int newScore, int player);
    public void addScoreToPlayer(int deltaScore, int player);
    public void minusScoreToPlayer(int deltaScore, int player);
    public int getScore(int player);
}
