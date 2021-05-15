package scoreManager;

import gameHelper.FileLogHandler;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-13 10:08
 */
public class FileLogScoreDecorator extends ScoreManagerDecorator {

    private final FileLogHandler fileLogHandler;

    public FileLogScoreDecorator(CardGameScoreManager wrappee, FileLogHandler fileLogHandler) {
        super(wrappee);
        this.fileLogHandler = fileLogHandler;
    }

    @Override
    public void initScores() {
        wrappee.initScores();
    }

    @Override
    public void updateScoreActorOfPlayer(int player) {
        wrappee.updateScoreActorOfPlayer(player);
    }

    @Override
    public int[] getScoreArray() {
        return wrappee.getScoreArray();
    }

    @Override
    public void setScore(int newScore, int player) {
        wrappee.setScore(newScore, player);
    }

    @Override
    public void addScoreToPlayer(int deltaScore, int player, String reason) {
        wrappee.addScoreToPlayer(deltaScore, player, reason);
        if(fileLogHandler == null) {
            System.out.println("logHandler for FileLogScoreDecorator is NULL");
            return;
        }
        fileLogHandler.writeMessageToLog("score,P" +
                player +
                "," +
                wrappee.getScore(player) +
                "," +
                deltaScore +
                "," +
                reason);
    }

    @Override
    public void minusScoreToPlayer(int deltaScore, int player, String reason) {
        wrappee.minusScoreToPlayer(deltaScore, player, reason);
        if(fileLogHandler == null) {
            System.out.println("logHandler for FileLogScoreDecorator is NULL");
            return;
        }
        fileLogHandler.writeMessageToLog("score,P" +
                player +
                "," +
                wrappee.getScore(player) +
                "," +
                deltaScore +
                "," +
                reason);
    }

    @Override
    public int getScore(int player) {
        return wrappee.getScore(player);
    }
}