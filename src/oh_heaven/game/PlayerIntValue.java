package oh_heaven.game;

public class PlayerIntValue extends Publisher {
    private int playerNumber;
    private int value;

    public PlayerIntValue(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void updateValue(int newValue, String type) {
        // Then notify all the Listeners observing this player as per the Observer pattern.
        for(int i = 0; i < super.getPlayerObservers().size(); i++) {
            super.getPlayerObservers().get(i).update(this.playerNumber, newValue, "score");
        }
    }
}
