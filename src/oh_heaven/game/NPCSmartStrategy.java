package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class NPCSmartStrategy<T> implements NPCStrategy, PlayerObserver<T>{
    int[] playerScores = new int[4];

    // placeholder
    public Card determineMove(Hand hand) {
        int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
        System.out.println(x);
        return hand.get(x);
    }

    public void update(int playerNum, T valueToUpdate, String type) {
        if(type.equals("score")) {
            playerScores[playerNum] = (int) valueToUpdate;
        } else if (type.equals("tricks")) {
            playerScores[playerNum] = (int) valueToUpdate;
        }
    }
}
