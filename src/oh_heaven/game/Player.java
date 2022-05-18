package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class Player {
    public Hand playerHand;
    public int playerNumber;
    public Card selected;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void initialisePlayerHand(Deck deck) {
        playerHand = new Hand(deck);
    }

    public void dealCard(Card dealt) {
        playerHand.insert(dealt, false);
    }

    public void sortHand() {
        playerHand.sort(Hand.SortType.SUITPRIORITY, true);
    }

    public void displayHand(CardGame cardGame, RowLayout layout, TargetArea targetArea) {
        playerHand.setView(cardGame, layout);
        playerHand.setTargetArea(targetArea);
        playerHand.draw();
    }
}
