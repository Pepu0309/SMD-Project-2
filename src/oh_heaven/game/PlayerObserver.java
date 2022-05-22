package oh_heaven.game;

public interface PlayerObserver {
    public void updateObservedPlayerScore(int observedPlayerScore);

    // This one doesn't need an argument because it just increments by 1 everytime.
    public void updateObservedPlayerTricksWon(int observedPlayerTricksWon);
}
