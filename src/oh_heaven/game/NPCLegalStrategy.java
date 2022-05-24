package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class NPCLegalStrategy<T> implements NPCStrategy, PlayerObserver<T>{

    int numPlayers = Oh_Heaven.nbPlayers;

    Card curLeadingMove;

    public NPCLegalStrategy(Player[] players) {
        for(int i = 0; i < numPlayers; i++) {
            players[i].addObserver(this);
        }
    }

    // Legal strategy will play a card of the same suit as the leading suit if it has at least one card
    // of the same suit, otherwise it will play a random card
    public Card determineMove(Hand hand, boolean leadingMove) {
        Card move;
        // extracting all the players card
        ArrayList<Card> sameSuit = hand.getCardsWithSuit(curLeadingMove.getSuit());
        if (! sameSuit.isEmpty()) {
            move = Oh_Heaven.randomCard(sameSuit);
        }
        else {
            move = Oh_Heaven.randomCard(hand);
        }
        return move;
    }

    public void update(int playerNum, T valueToUpdate, String type) {
        if (type.equals("leading move")) {
            curLeadingMove = (Card) valueToUpdate;
        }
    }
}
