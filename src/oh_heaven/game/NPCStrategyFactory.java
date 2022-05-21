package oh_heaven.game;

public class NPCStrategyFactory {
    private static NPCStrategyFactory instance = new NPCStrategyFactory();

    private NPCStrategyFactory() {};

    public static NPCStrategyFactory getInstance() {
        return instance;
    }

    public NPCStrategy createStrategy(String NPCStrategyName) {
        // Modify this later when legal and smart are created.
        if(NPCStrategyName.equals("random")) {
            return new NPCRandomStrategy();
        } else if(NPCStrategyName.equals("legal")) {
            return new NPCRandomStrategy();
        } else if (NPCStrategyName.equals("smart")) {
            return new NPCRandomStrategy();
        } else {
           return null;
        }
    }
}
