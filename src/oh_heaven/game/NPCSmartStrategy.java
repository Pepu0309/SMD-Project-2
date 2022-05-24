package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class NPCSmartStrategy<T> implements NPCStrategy, PlayerObserver<T>{
    int numPlayers = Oh_Heaven.nbPlayers;

    int[] playerScores = new int[numPlayers];
    int[] playersTricksWon = new int[numPlayers];
    int[] playerBids = new int[numPlayers];

    ArrayList<Card>[] playerHasPlayed;
    Card curLeadingMove;

    public NPCSmartStrategy(Player[] players) {
        for(int i = 0; i < numPlayers; i++) {
            players[i].addObserver(this);
        }

        initPlayerHasPlayed();
    }

    public void initPlayerHasPlayed() {
        for(int i = 0; i < numPlayers; i++) {
            playerHasPlayed[i] = new ArrayList<>();
        }
    }

    // Smart strategy will choose to use whether it's move is leading or not to determine what card to play.
    public Card determineMove(Hand hand, boolean leadingMove) {
        int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
        System.out.println(x);
        return hand.get(x);
    }

    public void update(int playerNum, T valueToUpdate, String type) {
        if(type.equals("score")) {
            playerScores[playerNum] = (int) valueToUpdate;
        } else if (type.equals("tricks")) {
            playersTricksWon[playerNum] = (int) valueToUpdate;
        } else if (type.equals("bids")) {
            playerBids[playerNum] = (int) valueToUpdate;
        } else if (type.equals("normal move")) {
            playerHasPlayed[playerNum].add((Card) valueToUpdate);
        } else if (type.equals("leading move")) {
            curLeadingMove = (Card) valueToUpdate;
            playerHasPlayed[playerNum].add((Card) valueToUpdate);
        }
    }
}
