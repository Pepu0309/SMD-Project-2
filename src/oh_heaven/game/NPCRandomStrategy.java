package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class NPCRandomStrategy implements NPCStrategy{

    // The random strategy simply ignores the leadingMove flag.
    public Card determineMove(Hand hand, boolean leadingMove) {
        int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

}
