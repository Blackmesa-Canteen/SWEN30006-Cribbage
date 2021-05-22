package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 10:04
 */
public class ShowPhasePairScoreStrategy implements CribbageScoreStrategy {
    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;


    public ShowPhasePairScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {

        final int score_for_pair2 = 2;
        final int score_for_pair3 = 6;
        final int score_for_pair4 = 12;

        Hand calculationHand = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for (Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for (Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        /* find pairs */
        Hand[] pair2s = calculationHand.extractPairs();
        Hand[] pair3s = calculationHand.extractTrips();
        Hand[] pair4s = calculationHand.extractQuads();

        for (Hand hand : pair2s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair2, player, "pair2," + cardInfoManager.canonical(hand));
        }

        for (Hand hand : pair3s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair3, player, "pair3," + cardInfoManager.canonical(hand));
        }

        for (Hand hand : pair4s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair4, player, "pair4," + cardInfoManager.canonical(hand));
        }
    }
}