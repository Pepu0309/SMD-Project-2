package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;

import static ch.aplu.util.BaseTimer.delay;

public class InteractivePlayer extends Player{
    private CardListener cardListener;

    public InteractivePlayer(int playerNumber) {
        super(playerNumber);
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
    // selected is simply returned.
    public Card playMove() {
        return this.selected;
    }

}
