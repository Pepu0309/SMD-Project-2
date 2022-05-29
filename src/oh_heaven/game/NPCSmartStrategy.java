package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class NPCSmartStrategy<T> implements NPCStrategy, PlayerObserver<T>{

    private int numPlayers = Oh_Heaven.nbPlayers;
    private int playerID;

    private int[] playerScores = new int[numPlayers];
    private int[] playersTricksWon = new int[numPlayers];
    private int[] playerBids = new int[numPlayers];
    private Card[] curTrick = new Card[numPlayers];
    private int nbCardsCurTrick = 0;
    private ArrayList<ArrayList<Card>> playerHasPlayed = new ArrayList<ArrayList<Card>>();

    private Oh_Heaven.Suit trumpSuit;
    private Oh_Heaven.Suit leadSuit;

    public NPCSmartStrategy(Player[] players, int playerID) {
        for(int i = 0; i < numPlayers; i++) {
            players[i].addObserver(this);
        }
        this.playerID = playerID;
        initPlayerHasPlayed();
    }

    private void initPlayerHasPlayed() {
        for(int i = 0; i < numPlayers; i++) {
            playerHasPlayed.add(new ArrayList<Card>());
        }
    }

    public Card determineMove(Hand hand, boolean leadingMove, Oh_Heaven.Suit trumpSuit) {

        this.trumpSuit = trumpSuit;
        Card move = null;

        if (leadingMove) {
            // if leading move, play the highest value not trump suit card, to increase odds of winning
            // trick and prevent the high value card from being wasted in a trick where its suit is not the
            // lead suit
            move = getHighestRankCardNotTrumpSuit(hand);
            // if no cards remain with different suit to trump suit, play highest value trump suit card
            if (move == null) {
                move = hand.get(0);
            }
        } else {
            // get our highest ranking lead suit card
            move = getHighestRankCardWithSuit(hand, leadSuit);
            // if our highest ranking card of the same suit as the leading suit is worse
            // than a card already played in the current trick, play our worst leading suit card
            if (move != null && ! isBestCardSoFar(move)) {
                move = getLowestRankCardWithSuit(hand, leadSuit);
            }
            // we have no card of the lead suit
            if (move == null) {
                // get our highest ranking trumo suit card
                move = getHighestRankCardWithSuit(hand, trumpSuit);
                // if our best trump suit card is worse than a card already played in the current trick,
                // play our worst not trump suit card
                if (move != null && ! isBestCardSoFar(move)) {
                    move = getLowestRankCardNotTrumpSuit(hand);
                }
                // if no lead suit or trump suit cards we have lost the trick, play worthless card
                if (move == null) {
                    hand.sort(Hand.SortType.RANKPRIORITY, false);
                    move = hand.getLast();
                }
            }
        }
        return move;
    }

    private Card getHighestRankCardNotTrumpSuit(Hand hand) {
        Card card = null;
        // sorting such that highest rank cards are first
        hand.sort(Hand.SortType.RANKPRIORITY, false);
        for (int i = 0; (card = hand.get(i)) != null; i++) {
            if (card.getSuit() != trumpSuit) {
                break;
            }
        }
        return card;
    }

    private Card getLowestRankCardNotTrumpSuit(Hand hand) {
        Card card = null;
        // sorting such that highest rank cards are first
        hand.reverseSort(Hand.SortType.RANKPRIORITY, false);
        for (int i = 0; (card = hand.get(i)) != null; i++) {
            if (card.getSuit() != trumpSuit) {
                break;
            }
        }
        return card;
    }

    private Card getHighestRankCardWithSuit(Hand hand, Oh_Heaven.Suit suit) {
        Card card = null;
        // sorting such that highest rank cards are first
        hand.sort(Hand.SortType.RANKPRIORITY, false);
        for (int i = 0; (card = hand.get(i)) != null; i++) {
            if (card.getSuit() == suit) {
                break;
            }
        }
        return card;
    }


    private Card getLowestRankCardWithSuit(Hand hand, Oh_Heaven.Suit suit) {
        Card card = null;
        // sorting such that lowest rank cards are first
        hand.reverseSort(Hand.SortType.RANKPRIORITY, false);
        for (int i = 0; (card = hand.get(i)) != null; i++) {
            if (card.getSuit() == suit) {
                // returning hand to sorting such that highest rank cards are first before returning
                hand.sort(Hand.SortType.RANKPRIORITY, false);
                break;
            }
        }
        return card;
    }

    private boolean cardCmp(Card card1, Card card2) {
        // if one card is the trump suit and the other isn't, the trump suit card is always better
        if (card1.getSuit().equals(trumpSuit) && ! card2.getSuit().equals(trumpSuit)) {
            return true;
        } else if (! card1.getSuit().equals(trumpSuit) && card2.getSuit().equals(trumpSuit)) {
            return false;
        // either both are the trump suit or neither are the trump suit, highest rank card wins
        // highest rank card starts at 0, so using '<' logical operator
        } else {
            return card1.getRankId() < card2.getRankId();
        }
    }

    private boolean isBestCardSoFar(Card candidateCard) {
        // if our candidate card is better than all currently played cards, we play it,
        // our card being better means it would win over the other cards if the trick
        // ended with just the currently played cards and our candidate card
        for (int i = 0; i < nbCardsCurTrick; i++) {
            // return false if any card in current trick is better
            if (! cardCmp(candidateCard, curTrick[i])) {
                return false;
            }
        }
        return true;
    }

    // store updates from other players via observer pattern
    public void update(int playerNum, T valueToUpdate, String type) {
        if (type.equals("score")) {
            playerScores[playerNum] = (int) valueToUpdate;
        } else if (type.equals("tricks")) {
            playersTricksWon[playerNum] = (int) valueToUpdate;
            nbCardsCurTrick = 0;
        } else if (type.equals("bids")) {
            playerBids[playerNum] = (int) valueToUpdate;
        } else if (type.equals("normal move")) {
            playerHasPlayed.get(playerNum).add((Card) valueToUpdate);
            curTrick[nbCardsCurTrick] = (Card) valueToUpdate;
            nbCardsCurTrick++;
        } else if (type.equals("leading move")) {
            leadSuit = (Oh_Heaven.Suit) ((Card) valueToUpdate).getSuit();
            playerHasPlayed.get(playerNum).add((Card) valueToUpdate);
            curTrick[nbCardsCurTrick] = (Card) valueToUpdate;
            nbCardsCurTrick++;
        }
    }
}
