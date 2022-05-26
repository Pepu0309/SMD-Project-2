package oh_heaven.game;

public interface PlayerObserver<T> {
    public void update(int playerNum, T valueToUpdate, String type);
}
