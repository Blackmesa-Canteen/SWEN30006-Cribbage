package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface CribbageScoreStrategy {

    void calcPlayScore(int player, Card newCard, Hand segmentHand);

    void calcShowScore(int player, Hand playerHand, Hand starter);

}
