package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:34
 */
public class RunScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public RunScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcPlayScore(int player, Card newCard, Hand segmentHand) {

    }

    @Override
    public void calcShowScore(int player, Hand playerHand, Hand starter) {

    }
}