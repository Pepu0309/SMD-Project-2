package oh_heaven.game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private int playerNumber;

    // "Players must not share information directly and must store their own information about the game they are
    // playing."
    private int playerScore;
    private int tricksWon;
    private int playerBid;

    private Hand playerHand;
    protected Card selected = null;

    private List<PlayerObserver> playerObservers = new ArrayList<>();

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

    public abstract Card playMove();

    public void resetMove() {
        this.selected = null;
    }

    public void addObserver(PlayerObserver playerObserver) {
        playerObservers.add(playerObserver);
    }

    public void removeObserver(PlayerObserver playerObserver) {
        playerObservers.remove(playerObserver);
    }


    public Hand getPlayerHand() {
        return playerHand;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void updatePlayerScore() {
        // Update the player's score by their tricksWon this round
        this.playerScore += this.tricksWon;

        if(this.tricksWon == this.playerBid) {
            this.playerScore += Oh_Heaven.madeBidBonus;
        }

        // Then notify all the Listeners observing this player as per the Observer pattern.
        for(int i = 0; i < playerObservers.size(); i++) {
            playerObservers.get(i).updateObservedPlayerScore(this.playerScore);
        }
    }

    public int getTricksWon() {
        return tricksWon;
    }

    public void updateTricksWon(int tricksWon) {
        this.tricksWon = tricksWon;
        for(int i = 0; i < playerObservers.size(); i++) {
            playerObservers.get(i).updateObservedPlayerTricksWon(tricksWon);
        }
    }

    public void placeBid() {
        this.playerBid = Oh_Heaven.nbStartCards / 4 + Oh_Heaven.random.nextInt(2);
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
}
