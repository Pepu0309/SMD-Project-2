package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class NPCSmartStrategy<T> implements NPCStrategy, PlayerObserver<T>{

    int numPlayers = Oh_Heaven.nbPlayers;

    int[] playerScores = new int[numPlayers];
    int[] playersTricksWon = new int[numPlayers];
    int[] playerBids = new int[numPlayers];
    ArrayList<ArrayList<Card>> playerHasPlayed = new ArrayList<ArrayList<Card>>();
    Card curLeadingMove;

    public NPCSmartStrategy(Player[] players) {
        for(int i = 0; i < numPlayers; i++) {
            players[i].addObserver(this);
        }

        initPlayerHasPlayed();
    }

    public void initPlayerHasPlayed() {
        for(int i = 0; i < numPlayers; i++) {
            playerHasPlayed.add(new ArrayList<Card>());
        }
    }

    // Smart strategy will choose to use whether it's move is leading or not to determine what card to play.
    public Card determineMove(Hand hand, boolean leadingMove, Oh_Heaven.Suit trumpSuit) {
        Card move = null;
        // sorting hand in descending order of rank
        hand.sort(Hand.SortType.RANKPRIORITY, false);
        // if leading move, play the highest value not trump suit card, to increase odds of winning
        // trick and prevent the high value card from being wasted in a trick where its suit is not the
        // lead suit
        if (leadingMove) {
            Card temp = null;
            for (int i = 0; (temp = hand.get(i)) != null; i++) {
                if (temp.getSuit() != trumpSuit) {
                    move = temp;
                    break;
                }
            }
            // if no cards remain with different suit to trump suit, play highest value trump suit card
            if (move == null) {
                move = hand.get(0);
            }
        } else {
            // if we possess a card with same suit as leading suit we must play it, so
            // play highest ranking card of that suit
            move = getHighestRankCardWithSuit(hand, (Oh_Heaven.Suit) curLeadingMove.getSuit());
            // else play the highest ranking trump suit card
            if (move == null) {
                move = getHighestRankCardWithSuit(hand, trumpSuit);
            }
            // if no lead suit or trump suit cards we have lost the trick, play worthless card to get rid
            if (move == null) {
                move = hand.getLast();
            }
        }
        return move;
    }

    // assumes hand sorted in descending order of rank
    private Card getHighestRankCardWithSuit(Hand hand, Oh_Heaven.Suit suit) {
        Card card;
        for (int i = 0; (card = hand.get(i)) != null; i++) {
            if (card.getSuit() == suit) {
                return card;
            }
        }
        return null;
    }

    public void update(int playerNum, T valueToUpdate, String type) {
        if (type.equals("score")) {
            playerScores[playerNum] = (int) valueToUpdate;
        } else if (type.equals("tricks")) {
            playersTricksWon[playerNum] = (int) valueToUpdate;
        } else if (type.equals("bids")) {
            playerBids[playerNum] = (int) valueToUpdate;
        } else if (type.equals("normal move")) {
            playerHasPlayed.get(playerNum).add((Card) valueToUpdate);
        } else if (type.equals("leading move")) {
            curLeadingMove = (Card) valueToUpdate;
            playerHasPlayed.get(playerNum).add((Card) valueToUpdate);
        }
    }
}
