package oh_heaven.game;

import ch.aplu.jcardgame.*;

public abstract class Player {
    private int playerNumber;

    // "Players must not share information directly and must store their own information about the game they are
    // playing."
    private Hand playerHand;
    protected Card selected = null;

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

    public abstract Card playMove();

    public void resetMove() {
        this.selected = null;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

}
