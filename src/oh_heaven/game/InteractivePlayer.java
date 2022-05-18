package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;

public class InteractivePlayer extends Player{

    public InteractivePlayer(int playerNumber) {
        super(playerNumber);
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card; playerHand.setTouchEnabled(false); }
        };
        playerHand.addCardListener(cardListener);
    }
}
