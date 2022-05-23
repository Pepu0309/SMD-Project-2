package oh_heaven.game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Publisher {
    private int playerNumber;

    // "Players must not share information directly and must store their own information about the game they are
    // playing."
    private PlayerIntValue playerScore;
    private int tricksWon;
    private int playerBid;

    private int nbStartCards;
    private Hand playerHand;
    protected Card selected = null;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.playerScore.setValue(0);
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

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getPlayerScore() {
        return playerScore.getValue();
    }

    public void updatePlayerScore() {
        int updateVal;
        // Update the player's score by their tricksWon this round
        updateVal = this.playerScore.getValue() + this.tricksWon;

        if(this.tricksWon == this.playerBid) {
            updateVal = this.playerScore.getValue() + Oh_Heaven.madeBidBonus;
        }

        this.playerScore.updateValue(updateVal, "score");

    }

    public int getTricksWon() {
        return tricksWon;
    }

    public void updateTricksWon(int tricksWon) {
        this.tricksWon = tricksWon;
        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, this.tricksWon, "tricks");
        }
    }

    public void placeBid() {
        this.playerBid = nbStartCards / 4 + Oh_Heaven.random.nextInt(2);
        System.out.println("Player " + this.playerNumber + " has bid " + this.playerBid);
    }

    public int getPlayerBid() {
        return playerBid;
    }

    public void enforcePlayerBid() {
        if (this.playerBid == 0) {
            this.playerBid = 1;
        } else {
            this.playerBid += Oh_Heaven.random.nextBoolean() ? -1 : 1;
        }
    }

    public void setNbStartCards(int nbStartCards)
    {
        this.nbStartCards = nbStartCards;
    }


}
