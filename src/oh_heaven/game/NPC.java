package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class NPC extends Player{

    public NPCStrategy strategy;

    public NPC(int playerNumber) {
        super(playerNumber);
    }

    public NPC(int playerNumber, String NPCStrategyName) throws Exception {
        super(playerNumber);
        try {
            strategy = NPCStrategyFactory.getInstance().createStrategy(NPCStrategyName);
            if(strategy == null) {
                throw new Exception("Invalid strategy");
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.exit(0);
        }

    }

    // Remove this when property loader is implemented and creation of strategy is done.
    public static Card randomCard(Hand hand){
        int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
        System.out.println(x);
        return hand.get(x);
    }

    public Card playMove() {
        // selected = strategy.determineMove(Hand hand);
        selected = randomCard(super.getPlayerHand());
        return selected;
    }
}
