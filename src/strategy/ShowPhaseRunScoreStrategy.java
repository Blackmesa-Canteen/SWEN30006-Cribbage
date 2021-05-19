package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:34
 */
public class ShowPhaseRunScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public ShowPhaseRunScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
        Hand calculationHand = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);

        int handSize = calculationHand.getNumberOfCards();

        /* cards in hand should more than 3, then can count runs*/
        if(handSize < 3) return;

        for(int runNum = 3; runNum <= handSize; runNum++) {
            Hand[] runHands = calculationHand.extractSequences(runNum);

            // if there are any runs
            for(Hand hand: runHands) {
                scoreManager.addScoreToPlayer(runNum, player,
                        "run" + runNum + "," + cardInfoManager.canonical(hand));
            }

            /* only 5 cards, so only one run */
            /* if one run occurs, then no any other runs */
            if(runHands.length > 0) break;
        }
    }

    private ArrayList<Hand> myExtractSquences(Hand theHand) {

        ArrayList<Hand> res = new ArrayList<>();

        /* handle JQKA problem */
        Hand handForCalc = new Hand(deck);
        for(Card card : theHand.getCardList()) {
            handForCalc.insert(card.clone(), false);
        }

        /* 先排序 */
        handForCalc.sort(Hand.SortType.POINTPRIORITY, false);
        ArrayList<Card> cardsForCalc = handForCalc.getCardList();

        /* 排序完后使用快慢指针找最大顺子 */
        int slow_ptr = 0;
        int quick_ptr = slow_ptr + 1;

        while(slow_ptr < cardsForCalc.size() - 1 && quick_ptr < cardsForCalc.size()) {

            /* 跳过相同等级的rank，让快指针找到不同的rank */
            while(cardInfoManager.getCardOrder(cardsForCalc.get(quick_ptr))
                    == cardInfoManager.getCardOrder(cardsForCalc.get(slow_ptr))) {
                quick_ptr++;
            }

            /* 如果不同rank，相差大于1了，慢指针移到当前位置，重新开始循环 */
            if((cardInfoManager.getCardOrder(cardsForCalc.get(quick_ptr))
                    - cardInfoManager.getCardOrder(cardsForCalc.get(slow_ptr))) > 1) {
                slow_ptr = quick_ptr;
                quick_ptr = slow_ptr + 1;
                continue;
            }
            else
            {
                /* 如果顺起来了1个，快指针继续移动 */


            }


            do {
                ;
            } while(quick_ptr < handForCalc.getNumberOfCards());
        }

        // AAJQK A*JQK A**QK A***K
        // retrieve default buggy sequence: AAJQK A*JQK A**QK
        Hand[] buggyHands = theHand.extractSequences(3);

        if(handForCalc.getFirst().getRank().equals(CribbageCardInfoManager.Rank.ACE) &&
                handForCalc.getLast().getRank().equals(CribbageCardInfoManager.Rank.KING)){

        }

        return res;
    }

    /* create own run finder */
    private Hand myGetRunsInShow(Hand theHand) {
        Hand res = null;

        for (int runNum = 3; runNum <= theHand.getNumberOfCards(); runNum++) {

            /* find official defined runs */
            Hand[] runHands = theHand.extractSequences(runNum);

            /* handle A10JQK A*JQK A**QK */
            for(Hand hand : runHands) {
                /* sort into A2345678910JQK */
                /* handle JQKA situation, after sorting: AJQK */
                hand.sort(Hand.SortType.POINTPRIORITY, false);

                if(hand.getFirst().getRank().equals(CribbageCardInfoManager.Rank.ACE) &&
                        hand.getLast().getRank().equals(CribbageCardInfoManager.Rank.KING)) {

                    // remove redundant A at the first
                    hand.removeFirst(false);

                    if(hand.getNumberOfCards() >= 3) {
                        // JQK, 10JQK, they are runs
                        res = hand;
                        /* only one run in the show phase (5 cards in total) */
                        return res;
                    }
                }
            }

            /* handle A23**, A234*, A2345, AA23*, AA234, AAA23  */
            // clone original hand and sort
            Hand handForCalc = new Hand(deck);
            for(Card card : theHand.getCardList()) {
                handForCalc.insert(card.clone(), false);
            }

            handForCalc.sort(Hand.SortType.POINTPRIORITY, false);


            if(handForCalc.getFirst().getRank().equals(CribbageCardInfoManager.Rank.ACE)) {
                int index = 0;
                Hand buffer = new Hand(deck);

                // move index to first non-A card
                while(handForCalc.getCard(index).getRank().equals(CribbageCardInfoManager.Rank.ACE)) {
                    index++;
                }

                int other_cards_left = theHand.getNumberOfCards() - index;

                // add the first A to the buffer
                buffer.insert(handForCalc.getCard(index - 1).clone() , false);

                if(other_cards_left >= 2) {
                    /* at least there are 2 other card ranks */
                    for(int i = 2; i < other_cards_left; i++) {

                    }


                }

            }


        }




        return res;
    }
}