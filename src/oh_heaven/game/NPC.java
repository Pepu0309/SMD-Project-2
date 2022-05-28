package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class NPC extends Player {

    public NPCStrategy strategy;

    public NPC(int playerNumber) throws Exception {
        super(playerNumber);
    }

    // Set the strategy for this NPC instance
    public void initStrategy(String NPCStrategyName, Player[] players) {
        try {
            strategy = NPCStrategyFactory.getInstance().createStrategy(NPCStrategyName, players);
            if(strategy == null) {
                throw new Exception("Invalid strategy");
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.exit(0);
        }
    }

    // The leadingMove boolean is passed to the strategy the NPC uses and might be used as part of determining
    // the move.
    public Card playMove(boolean leadingMove, Oh_Heaven.Suit trumpSuit) {

        this.selected = strategy.determineMove(super.getPlayerHand(), leadingMove, trumpSuit);
        notifyMove(this.selected, leadingMove);
        return this.selected;

    }

}
