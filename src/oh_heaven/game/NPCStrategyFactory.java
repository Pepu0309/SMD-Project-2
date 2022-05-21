package oh_heaven.game;

public class NPCStrategyFactory {
    private static NPCStrategyFactory instance = new NPCStrategyFactory();

    private NPCStrategyFactory() {};

    public static NPCStrategyFactory getInstance() {
        return instance;
    }

    public NPCStrategy createStrategy(String NPCStrategyName) {
        // Modify this later when legal and smart are created.
        if(NPCStrategyName == "legal") {
            return new NPCRandomStrategy();
        } else if (NPCStrategyName == "smart") {
            return new NPCRandomStrategy();
        // Otherwise, create an NPC with a random strategy. This behaviour also means that an NPC with random
        // strategy will be created with NPCStrategyName == "random" or something else (which would be invalid).
        } else {
            return new NPCRandomStrategy();
        }
    }
}
