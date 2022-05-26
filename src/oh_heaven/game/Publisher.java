package oh_heaven.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Publisher {
    private List<PlayerObserver> playerObservers = new ArrayList<>();

    public void addObserver(PlayerObserver playerObserver) {
        playerObservers.add(playerObserver);
    }

    public void removeObserver(PlayerObserver playerObserver) {
        playerObservers.remove(playerObserver);
    }

    public List<PlayerObserver> getPlayerObservers() {
        return playerObservers;
    }
}
