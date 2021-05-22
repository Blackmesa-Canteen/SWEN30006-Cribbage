package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:34
 */
public class PlayPhaseRunScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public PlayPhaseRunScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
        final int score_for_run3 = 3;
        final int score_for_run4 = 4;
        final int score_for_run5 = 5;
        final int score_for_run6 = 6;
        final int score_for_run7 = 7;

        Hand calculationHand = new Hand(deck);
        ArrayList<Card> cards = calculationHand.getCardList();
        int runNum =1;
        if (cards.size() > 2) {
            int maxRun = cards.size();
            if (cards.size() > 7) {
                maxRun = 7;
            }
            int i$;
            for (i$ = maxRun; i$ > 0; i$--) {
                calculationHand.insert(cards.get(i$ - 1), false);
            }
            runNum = maxRun;
            mainloop:
            for (i$ = 0; i$ < maxRun; i$++) {
                calculationHand.sort(Hand.SortType.POINTPRIORITY, false);
                ArrayList<Card> sortCards = calculationHand.getCardList();
                for (int j$ = sortCards.size() - 1; j$ > 0; j$--) {
                    if (cardInfoManager.getCardOrder(sortCards.get(j$)) !=
                            cardInfoManager.getCardOrder(sortCards.get(j$ - 1)) + 1) {
                        calculationHand.remove(cards.get(maxRun - runNum), false);
                        runNum -= 1;
                        continue mainloop;
                    }
                }
            }
        }
        int scores = 0;
        switch (runNum) {
            case 3:
                scores = score_for_run3;
                scoreManager.addScoreToPlayer(scores, player, "run3");
                break;
            case 4:
                scores = score_for_run4;
                scoreManager.addScoreToPlayer(scores, player, "run4");
                break;
            case 5:
                scores = score_for_run5;
                scoreManager.addScoreToPlayer(scores, player, "run5");
                break;
            case 6:
                scores = score_for_run6;
                scoreManager.addScoreToPlayer(scores, player, "run6");
                break;
            case 7:
                scores = score_for_run7;
                scoreManager.addScoreToPlayer(scores, player, "run7");
                break;
            default:
                break;
        }
    }
}