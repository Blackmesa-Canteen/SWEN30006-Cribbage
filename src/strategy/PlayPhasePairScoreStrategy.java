package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

import java.util.ArrayList;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-14 20:47
 */
public class PlayPhasePairScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;


    public PlayPhasePairScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
        final int score_for_pair2 = 2;
        final int score_for_pair3 = 6;
        final int score_for_pair4 = 12;

        ArrayList<Card> cards = segmentHand.getCardList();
        int pairNum = 1;
        int scores = 0;

        int tailPtr = cards.size() - 1;

        /* find continuous pairs from tail of the segment hand */
        for(int i = tailPtr; i > 0; i--){
            if(cards.get(i).getRank().equals(cards.get(i -1).getRank())) {
                pairNum++;
            } else {
                break;
            }
        }

        switch (pairNum) {
            case 2:
                scores = score_for_pair2;
                scoreManager.addScoreToPlayer(scores, player, "pair2");
                break;
            case 3:
                scores = score_for_pair3;
                scoreManager.addScoreToPlayer(scores, player, "pair3");
                break;
            case 4:
                scores = score_for_pair4;
                scoreManager.addScoreToPlayer(scores, player, "pair4");
                break;
            default:
                break;
        }

    }

}