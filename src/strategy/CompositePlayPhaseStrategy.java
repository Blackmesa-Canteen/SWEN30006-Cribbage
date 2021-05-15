package strategy;

import ch.aplu.jcardgame.Hand;

/**
 * @author Xiaotian
 * @program Cribbage
 * @description
 * @create 2021-05-15 10:16
 */
public class CompositePlayPhaseStrategy extends CompositeCribbageScoreStrategy{

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
       for(CribbageScoreStrategy strategy : strategies) {
           /* traverse all strategy for play phase */
           strategy.calcScore(player, segmentHand, starter);
       }
    }
}