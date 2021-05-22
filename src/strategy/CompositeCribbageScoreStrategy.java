package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

import java.util.ArrayList;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:49
 */
public abstract class CompositeCribbageScoreStrategy implements CribbageScoreStrategy{


    protected ArrayList<CribbageScoreStrategy> strategies = new ArrayList<>();

    public void addStrategy(CribbageScoreStrategy playStrategy) {
        strategies.add(playStrategy);
    }

    @Override
    public abstract void calcScore(int player, Hand segmentHand, Hand starter);
}