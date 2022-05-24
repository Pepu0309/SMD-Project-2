package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class InteractivePlayer extends Player{
    private CardListener cardListener;

    public InteractivePlayer(int playerNumber) {
        super(playerNumber);
    }

    // For interactive player, after initialising the hand, also add a card listener to the hand.
    public void initialisePlayerHand(Deck deck) {
        super.initialisePlayerHand(deck);
        // Get the hand through calling superclass method as the hand of the player is a private attribute.
        Hand playerHand = super.getPlayerHand();

        // Set up human player for interaction
        cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                playerHand.setTouchEnabled(false);
            }
        };
        playerHand.addCardListener(cardListener);
    }

    // selected should be set by the leftDoubleClicked function, so for the interactive player to play a move,
    // selected is simply returned. The interactive player ignores the leadingMove.
    public Card playMove(boolean leadingMove) {
        notifyMove(this.selected, leadingMove);
        return this.selected;
    }

}
