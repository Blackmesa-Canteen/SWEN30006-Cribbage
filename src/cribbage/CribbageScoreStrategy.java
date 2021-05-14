package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface CribbageScoreStrategy {

    int getPlayScore(Card newCard, Hand previousSegmentHand);

    int getShowScore(Hand PlayerHand, Hand starter);

}
