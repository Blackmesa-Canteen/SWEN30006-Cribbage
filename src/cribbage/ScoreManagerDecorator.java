package cribbage;

/**
 * @author Xiaotian
 * @program Cribbage
 * @description
 * @create 2021-05-13 10:05
 */
public abstract class ScoreManagerDecorator implements CardGameScoreManager{
    protected CardGameScoreManager wrappee;

    public ScoreManagerDecorator(CardGameScoreManager wrappee) {
        this.wrappee = wrappee;
    }

    public abstract void initScores();
    public abstract void updateScoreActorOfPlayer(int player);
    public abstract int[] getScoreArray();
    public abstract void setScore(int newScore, int player);
    public abstract void addScoreToPlayer(int deltaScore, int player, String reason);
    public abstract void minusScoreToPlayer(int deltaScore, int player, String reason);
    public abstract int getScore(int player);


}