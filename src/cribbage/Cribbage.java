package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import factory.CardGameScoreManagerFactory;
import factory.CribbageScoreStrategyFactory;
import gameHelper.CribbageCardInfoManager;
import gameHelper.FileLogHandler;
import scoreManager.CardGameScoreManager;
import gameHelper.CardHistoryManager;
import scoreManager.ConsoleLogScoreDecorator;
import scoreManager.FileLogScoreDecorator;
import strategy.CribbageScoreStrategy;
import strategy.PlayPhasePairScoreStrategy;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Cribbage extends CardGame {
    static Cribbage cribbage;  // Provide access to singleton

    private CardGameScoreManager scoreManager;
    private final CribbageCardInfoManager cribbageCardInfoManager;
    private final FileLogHandler fileLogHandler;
    private final CardHistoryManager historyManager;



    static Random random;

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    static boolean ANIMATE;

    /**
     * transfer the card c to new hand h
     * @param c
     * @param h
     */
    void transfer(Card c, Hand h) {
        if (ANIMATE) {
            c.transfer(h, true);
        } else {
            c.removeFromHand(true);
            h.insert(c, true);
        }
    }

    private void dealingOut(Hand pack, Hand[] hands) {
        for (int i = 0; i < nStartCards; i++) {
            for (int j = 0; j < nPlayers; j++) {
                Card dealt = randomCard(pack);
                dealt.setVerso(false);  // Show the face
                transfer(dealt, hands[j]);
            }
        }
    }

    static int SEED;

    public static Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    private final String version = "0.1";
    static public final int nPlayers = 2;
    public final int nStartCards = 6;
    public final int nDiscards = 2;
    private final int handWidth = 400;
    private final int cribWidth = 150;
    private final int segmentWidth = 180;
    private final Deck deck = new Deck(CribbageCardInfoManager.Suit.values(), CribbageCardInfoManager.Rank.values(), "cover", new CribbageCardInfoManager.MyCardValues());
    private final Location[] handLocations = {
            new Location(360, 75),
            new Location(360, 625)
    };

    private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
            new Location(150, 350),
            new Location(400, 350),
            new Location(650, 350)
    };
    private final Location starterLocation = new Location(50, 625);
    private final Location cribLocation = new Location(700, 625);
    private final Location seedLocation = new Location(5, 25);
    // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
    private final Location textLocation = new Location(350, 450);
    private final Hand[] hands = new Hand[nPlayers];
    private Hand starter;
    private Hand crib;

    public static void setStatus(String string) {
        cribbage.setStatusText(string);
    }

    static private final IPlayer[] players = new IPlayer[nPlayers];

    final Font normalFont = new Font("Serif", Font.BOLD, 24);
    final Font bigFont = new Font("Serif", Font.BOLD, 36);

    private void deal(Hand pack, Hand[] hands) {
        System.out.println("deal");
        for (int i = 0; i < nPlayers; i++) {
            hands[i] = new Hand(deck);
            // players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
            players[i].setId(i);
            players[i].startSegment(deck, hands[i]);
        }
        RowLayout[] layouts = new RowLayout[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(0);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
        layouts[0].setStepDelay(0);

        dealingOut(pack, hands);
        for (int i = 0; i < nPlayers; i++) {
            hands[i].sort(Hand.SortType.POINTPRIORITY, true);
            fileLogHandler.writeMessageToLog("deal,P"+ i + ","+ cribbageCardInfoManager.canonical(hands[i]));
        }
        layouts[0].setStepDelay(0);
    }

    private void discardToCrib() {
        System.out.println("discard to Crib");
        crib = new Hand(deck);
        RowLayout layout = new RowLayout(cribLocation, cribWidth);
        layout.setRotationAngle(0);
        crib.setView(this, layout);
        // crib.setTargetArea(cribTarget);
        crib.draw();

        for (IPlayer player : players) {
            Card playerDiscardCard = null;
            Hand cardsDiscarded = new Hand(deck);
            StringBuilder playerDiscardLogStr = new StringBuilder("discard,P" + player.id + ",");
            for (int i = 0; i < nDiscards; i++) {
                playerDiscardCard = player.discard();
                /* store cloned Card obj to cardsDiscarded for sorting log */
                cardsDiscarded.insert(playerDiscardCard.clone(), false);
                transfer(playerDiscardCard, crib);
            }

            /* sort log */
            cardsDiscarded.sort(Hand.SortType.POINTPRIORITY, true);
            /* display log */
            playerDiscardLogStr.append(cribbageCardInfoManager.canonical(cardsDiscarded));
            fileLogHandler.writeMessageToLog(playerDiscardLogStr.toString());
            crib.sort(Hand.SortType.POINTPRIORITY, true);

        }
    }

    // if starter is a Jack, the dealer gets 2 points
    private void starter(Hand pack) {
        System.out.println("starter");
        starter = new Hand(deck);
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        starter.setView(this, layout);
        starter.draw();
        Card dealt = randomCard(pack);
        dealt.setVerso(false);

        fileLogHandler.writeMessageToLog("starter," + cribbageCardInfoManager.canonical(dealt));

        // player 1 is dealer
        if(dealt.getRank() == CribbageCardInfoManager.Rank.JACK) {
            System.out.println("starter card J 2 scores");
            scoreManager.addScoreToPlayer(2, 1,
                    "starter,[" + cribbageCardInfoManager.canonical(dealt) + "]");
        }

        transfer(dealt, starter);
    }

    int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += CribbageCardInfoManager.cardValue(c);
        return total;
    }

    class Segment {
        Hand segment;
        boolean go;
        int lastPlayer;
        boolean newSegment;

        void reset(final List<Hand> segments) {
            System.out.println("new segment");
            segment = new Hand(deck);
            segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
            segment.draw();
            go = false;        // No-one has said "go" yet
            lastPlayer = -1;   // No-one has played a card yet in this segment
            newSegment = false;  // Not ready for new segment yet
        }
    }

    private void play(CribbageScoreStrategy playPhaseStrategy) {
        System.out.println("play");

        /* create cardHistory for each player */
        for(int i = 0; i < nPlayers; i++) {
            historyManager.creatCardHistoryPile("player" + i);
        }

        final int thirtyone = 31;
        List<Hand> segments = new ArrayList<>();
        int currentPlayer = 0; // Player 1 is dealer
        Segment s = new Segment();
        s.reset(segments);
        while (true) {
            /* if all players have no cards, the player who placed the last card scores one point */
            if((players[0].emptyHand() && players[1].emptyHand())) {
                System.out.println("player " + currentPlayer + " has no cards.");
                System.out.println("player " + s.lastPlayer +" gets 1 go score");
                scoreManager.addScoreToPlayer(1, s.lastPlayer, "go");
                break;
            }

            // System.out.println("segments.size() = " + segments.size());
            Card nextCard = players[currentPlayer].lay(thirtyone - total(s.segment));
            System.out.println("player: " + currentPlayer +" playing");
            if (nextCard == null) {
                System.out.println("player " +currentPlayer+": " + s.go);
                if (s.go) {
                    // Another "go" after previous one with no intervening cards
                    // lastPlayer gets 1 point for a "go"
                    System.out.println("player " + s.lastPlayer +" gets 1 go score");
                    scoreManager.addScoreToPlayer(1, s.lastPlayer, "go");
                    s.newSegment = true;
                } else {
                    // currentPlayer says "go"
                    System.out.println("player " + currentPlayer + " says go");
                    s.go = true;
                }
                currentPlayer = (currentPlayer + 1) % 2;
            } else {
                s.lastPlayer = currentPlayer; // last Player to play a card in this segment

                /* record the card into history manager */
                System.out.println("player" +currentPlayer+" play: " + cribbageCardInfoManager.canonical(nextCard));
                historyManager.recordCardToHistoryPile("player" + currentPlayer, nextCard);

                transfer(nextCard, s.segment);
                fileLogHandler.writeMessageToLog("play,P" +
                        s.lastPlayer +
                        "," +
                        total(s.segment) +
                        "," +
                        cribbageCardInfoManager.canonical(nextCard));

                /* calc play phase score */
                playPhaseStrategy.calcScore(currentPlayer, s.segment, starter);

                if (total(s.segment) == thirtyone) {
                    // lastPlayer gets 2 points for a 31
                    /* has been calculated in strategy */
                    s.newSegment = true;
                    currentPlayer = (currentPlayer + 1) % 2;
                } else {
                    // if total(segment) == 15, lastPlayer gets 2 points for a 15
                    /* has been calculated in strategy */

                    if (!s.go) { // if it is "go" then same player gets another turn
                        currentPlayer = (currentPlayer + 1) % 2;
                    }
                }
            }
            if (s.newSegment) {
                segments.add(s.segment);
                s.reset(segments);
            }
        }
    }

    void showHandsCrib(CribbageScoreStrategy showPhaseStrategy) {
        System.out.println("show");
        // score player 0 (non dealer)
        showCards(0);

        /* calc pair score demo */
        showPhaseStrategy.calcScore(0,
                historyManager.getCardHistoryPile("player" +0),
                starter);

        // score player 1 (dealer)
        showCards(1);
        /* calc pair score demo */
        showPhaseStrategy.calcScore(1,
                historyManager.getCardHistoryPile("player" +1),
                starter);

        // score crib (for dealer)
        showCrib(1);
        /* calc pair score demo */
        showPhaseStrategy.calcScore(1,
                crib,
                starter);

    }

    private void showCards(int player) {
        if(player < nPlayers) {
            fileLogHandler.writeMessageToLog("show,P" +
                    player +
                    "," +
                    cribbageCardInfoManager.canonical(starter.getFirst()) +
                    "+" +
                    cribbageCardInfoManager.canonical(historyManager.getCardHistoryPile("player" +player)));
            return;
        }

        System.out.println("showCards: player id error");
    }

    private void showCrib(int dealer) {
        fileLogHandler.writeMessageToLog("show,P" +
                dealer +
                "," +
                cribbageCardInfoManager.canonical(starter.getFirst()) +
                "+" +
                cribbageCardInfoManager.canonical(crib));
    }

    public Cribbage() {
        super(850, 700, 30);
        cribbage = this;
        setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        /* init logHandler */
        fileLogHandler = new FileLogHandler("cribbage.log");
        fileLogHandler.initFP();

        fileLogHandler.writeMessageToLog("seed," + SEED);
        for(int i = 0 ; i < nPlayers ; i++) {
            fileLogHandler.writeMessageToLog(players[i].getPlayerType() + ",P" + i);
        }

        /* init card info */
        cribbageCardInfoManager = new CribbageCardInfoManager(deck);

        /* instantiate a ScoreManager for nPlayers */
        /* set up score Manager Factory */
        CardGameScoreManagerFactory scoreManagerFactory = CardGameScoreManagerFactory.getInstance();
        /* create a scoreManager for this Cribbage game */
        scoreManager = scoreManagerFactory.getScoreManager("Cribbage", nPlayers, this);
        scoreManager.initScores();

        /* DEBUG: decorate score manager with Console Log functions */
        scoreManager = new ConsoleLogScoreDecorator(scoreManager);

        /* decorate score manager with File Log functions */
        scoreManager = new FileLogScoreDecorator(scoreManager, fileLogHandler);

        /* init card History manager */
        historyManager = new CardHistoryManager(deck);

        /* init strategy */
        CribbageScoreStrategyFactory scoreStrategyFactory = CribbageScoreStrategyFactory.getInstance();

        CribbageScoreStrategy playPhaseStrategy = scoreStrategyFactory.getCompositePhaseStrategy(
                cribbageCardInfoManager,
                scoreManager,
                deck,
                "play"
        );

        CribbageScoreStrategy showPhaseStrategy = scoreStrategyFactory.getCompositePhaseStrategy(
                cribbageCardInfoManager,
                scoreManager,
                deck,
                "show"
        );


        Hand pack = deck.toHand(false);
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        pack.setView(this, layout);
        pack.setVerso(true);
        pack.draw();
        addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

        /* Play the round */
        deal(pack, hands);
        discardToCrib();
        starter(pack);
        play(playPhaseStrategy);
        showHandsCrib(showPhaseStrategy);

        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText("Game over.");
        fileLogHandler.closeFP();
        refresh();
    }

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        /* Handle Properties */
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Properties cribbageProperties = new Properties();
        // Default properties
        cribbageProperties.setProperty("Animate", "true");
        cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
        cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

        // Read properties
        try (FileReader inStream = new FileReader("cribbage.properties")) {
            cribbageProperties.load(inStream);
        }

        // Control Graphics
        ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

        // Control Randomisation
        /* Read the first argument and save it as a seed if it exists */
        if (args.length > 0) { // Use arg seed - overrides property
            SEED = Integer.parseInt(args[0]);
        } else { // No arg
            String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
            if (seedProp != null) { // Use property seed
                SEED = Integer.parseInt(seedProp);
            } else { // and no property
                SEED = new Random().nextInt(); // so randomise
            }
        }
        random = new Random(SEED);

        // Control Player Types
        Class<?> clazz;
        clazz = Class.forName(cribbageProperties.getProperty("Player0"));
        players[0] = (IPlayer) clazz.getConstructor(String.class).newInstance(cribbageProperties.getProperty("Player0"));
        clazz = Class.forName(cribbageProperties.getProperty("Player1"));
        players[1] = (IPlayer) clazz.getConstructor(String.class).newInstance(cribbageProperties.getProperty("Player1"));
        // End properties

        new Cribbage();
    }

}
