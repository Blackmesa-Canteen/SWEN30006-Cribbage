package cribbage;

import ch.aplu.jcardgame.CardGame;

/**
 * @program Cribbage
 * @description Factory for Score manager
 * @create 2021-05-11 12:29
 */
public class CardGameScoreManagerFactory {
    private static CardGameScoreManagerFactory instance;

    public static synchronized CardGameScoreManagerFactory getInstance() {
        if(instance == null) {
            instance = new CardGameScoreManagerFactory();
        }

        return instance;
    }

    public CardGameScoreManager getScoreManager(String cardGameName, int nPlayers, CardGame cardGame) {

        if(cardGameName.equals("Cribbage")) {
            return new CribbageScoreManager(nPlayers, cardGame);
        }

        return null;
    }
}