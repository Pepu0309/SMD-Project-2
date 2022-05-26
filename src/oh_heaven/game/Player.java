package oh_heaven.game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Publisher {
    private int playerNumber;

    // "Players must not share information directly and must store their own information about the game they are
    // playing."
    private int playerScore;
    private int tricksWon;
    private int playerBid;

    private int nbStartCards;
    private Hand playerHand;
    protected Card selected = null;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.playerScore = 0;
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

    public abstract Card playMove(boolean leadingMove, Oh_Heaven.Suit trumpSuit);

    public void notifyMove(Card cardPlayed, boolean leadingMove) {
        String moveType;
        if(leadingMove) {
            moveType = "leading move";
        } else {
            moveType = "normal move";
        }
        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, this.selected, moveType);
        }
    }

    public void resetMove() {
        this.selected = null;
    }

    public Hand getPlayerHand() {
        return this.playerHand;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public void updatePlayerScore() {
        // Update the player's score by their tricksWon this round
        this.playerScore += this.tricksWon;

        if(this.tricksWon == this.playerBid) {
            this.playerScore += Oh_Heaven.madeBidBonus;
        }

        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, this.playerScore, "score");
        }
    }

    public int getTricksWon() {
        return this.tricksWon;
    }

    public void updateTricksWon(int tricksWon) {
        this.tricksWon = tricksWon;
        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, this.tricksWon, "tricks");
        }
    }

    public void placeBid() {
        this.playerBid = nbStartCards / 4 + Oh_Heaven.random.nextInt(2);

        // This print statement is from base package
        System.out.println("Player " + this.playerNumber + " has bid " + this.playerBid);

        this.notifyBid();
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

        this.notifyBid();
    }

    private void notifyBid() {
        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, this.playerBid, "bids");
        }
    }

    public void setNbStartCards(int nbStartCards)
    {
        this.nbStartCards = nbStartCards;
    }

}
