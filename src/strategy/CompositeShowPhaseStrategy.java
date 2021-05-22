package strategy;

import ch.aplu.jcardgame.Hand;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 10:20
 */
public class CompositeShowPhaseStrategy extends CompositeCribbageScoreStrategy {
    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
        for(CribbageScoreStrategy strategy : strategies) {
            /* traverse all strategy for show phase */
            strategy.calcScore(player, segmentHand, starter);
        }
    }
}