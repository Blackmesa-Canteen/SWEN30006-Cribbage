package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:38
 */
public class JackScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public JackScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {

        final int jack_score = 1;

        Hand calculationHand = new Hand(deck);
        Hand handWithoutStarter = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
            handWithoutStarter.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);
        handWithoutStarter.sort(Hand.SortType.POINTPRIORITY, false);

        CribbageCardInfoManager.Suit starterSuit = (CribbageCardInfoManager.Suit) starter.getFirst().getSuit();
        /* get J s */
        Hand jFromHand = handWithoutStarter.extractCardsWithRank(CribbageCardInfoManager.Rank.JACK);

        if(jFromHand.getNumberOfCardsWithSuit(starterSuit) == 1) {
            /* if find one J that has the same suit as starter's  */
            Hand theOne = jFromHand.extractCardsWithSuit(starterSuit);
            scoreManager.addScoreToPlayer(jack_score, player,
                    "jack," + cardInfoManager.canonical(theOne));
        }
    }
}