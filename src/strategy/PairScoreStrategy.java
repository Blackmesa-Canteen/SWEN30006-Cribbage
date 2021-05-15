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
public class PairScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;


    public PairScoreStrategy(Deck deck, CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcPlayScore(int player, Card newCard, Hand segmentHand) {
        final int score_for_pair2 = 2;
        final int score_for_pair3 = 6;
        final int score_for_pair4 = 12;

        ArrayList<Card> cards = segmentHand.getCardList();
        int pairNum = 1;
        int scores = 0;

        int tailPtr = cards.size() - 1;

//        for(Card card : cards) {
//            if(card.getRank().equals(newCard.getRank())) {
//                pairNum++;
//            }
//        }

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
                break;
            case 3:
                scores = score_for_pair3;
                break;
            case 4:
                scores = score_for_pair4;
                break;
            default:
                break;
        }

        if(scores != 0) {
            scoreManager.addScoreToPlayer(scores, player, "pair");
        }
    }

    @Override
    public void calcShowScore(int player, Hand playerHand, Hand starter) {
        final int score_for_pair2 = 2;
        final int score_for_pair3 = 6;
        final int score_for_pair4 = 12;

        Hand calculationHand = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : playerHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        /* find pairs */
        Hand[] pair2s = calculationHand.extractPairs();
        Hand[] pair3s = calculationHand.extractTrips();
        Hand[] pair4s = calculationHand.extractQuads();

        for(Hand hand : pair2s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair2, player, "pair2," + cardInfoManager.canonical(hand));
        }

        for(Hand hand : pair3s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair3, player, "pair3," + cardInfoManager.canonical(hand));
        }

        for(Hand hand : pair4s) {
            hand.sort(Hand.SortType.POINTPRIORITY, false);
            scoreManager.addScoreToPlayer(score_for_pair4, player, "pair4," + cardInfoManager.canonical(hand));
        }
    }
}