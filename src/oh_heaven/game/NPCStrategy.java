package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface NPCStrategy {
    public Card determineMove(Hand hand);
}
