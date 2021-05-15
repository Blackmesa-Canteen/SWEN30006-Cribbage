package factory;

import ch.aplu.jcardgame.Deck;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;
import strategy.*;

/**
 * @author Xiaotian
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:43
 */
public class CribbageScoreStrategyFactory {
    private static CribbageScoreStrategyFactory instance;

    public static synchronized CribbageScoreStrategyFactory getInstance() {
        if(instance == null) {
            instance = new CribbageScoreStrategyFactory();
        }

        return instance;
    }

    public CribbageScoreStrategy getCompositePhaseStrategy(CribbageCardInfoManager cardInfoManager,
                                                           CardGameScoreManager scoreManager,
                                                           Deck deck,
                                                           String phaseName) {

        CribbageScoreStrategy res = null;
        CompositeCribbageScoreStrategy compositeStrategy = null;

        switch (phaseName) {

            case "play":
                compositeStrategy = new CompositePlayPhaseStrategy();

                compositeStrategy.addStrategy(
                        new PlayPhaseTotalPointsStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new PlayPhaseRunScoreStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new PlayPhasePairScoreStrategy(cardInfoManager, scoreManager, deck)
                );

                res = compositeStrategy;
                break;

            case "show":
                compositeStrategy = new CompositeShowPhaseStrategy();

                compositeStrategy.addStrategy(
                        new ShowPhaseTotalPointsStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new ShowPhaseRunScoreStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new ShowPhasePairScoreStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new FlushScoreStrategy(cardInfoManager, scoreManager, deck)
                );

                compositeStrategy.addStrategy(
                        new JackScoreStrategy(cardInfoManager, scoreManager, deck)
                );
                res = compositeStrategy;
                break;

            default:
                break;
        }

        return res;

    }
}