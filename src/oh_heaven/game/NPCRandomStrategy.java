package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class NPCRandomStrategy implements NPCStrategy{

    public Card determineMove(Hand hand) {
        int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
        System.out.println(x);
        return hand.get(x);
    }

}
