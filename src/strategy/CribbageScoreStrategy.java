package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface CribbageScoreStrategy {

//    void calcScore(int player, Card newCard, Hand segmentHand);

    void calcScore(int player, Hand segmentHand, Hand starter);

}
