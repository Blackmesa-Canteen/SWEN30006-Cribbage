package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-14 21:49
 */
public class TotalPointsScoreStrategy implements CribbageScoreStrategy {



    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public TotalPointsScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcPlayScore(int player, Card newCard, Hand segmentHand) {

        final int score_for_thirty_one = 2;
        final int score_for_fifteen = 2;

        Hand calculationHand = new Hand(deck);
        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        if (total(calculationHand) == 31) {
            // lastPlayer gets 'score_for_thirty_one' points for a 31
            System.out.println("strategy " + player + " gets "+ score_for_thirty_one + " score for 31 points");
            scoreManager.addScoreToPlayer(score_for_thirty_one, player, "thirtyone");
        }
        else if (total(calculationHand) == 15) {
            // lastPlayer gets 'score_for_fifteen' points for a 31
            System.out.println("strategy " + player + " gets "+ score_for_fifteen + " score for 31 points");
            scoreManager.addScoreToPlayer(score_for_fifteen, player, "thirtyone");
        }
    }

    @Override
    public void calcShowScore(int player, Hand playerHand, Hand starter) {

        // !!! TODO: total calc for Show phase

        final int score_for_fifteen = 2;

        Hand calculationHand = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : playerHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);



    }

    private int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += CribbageCardInfoManager.cardValue(c);
        return total;
    }

}