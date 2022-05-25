package oh_heaven.game;

public class NPCStrategyFactory {
    private static NPCStrategyFactory instance = new NPCStrategyFactory();

    private NPCStrategyFactory() {};

    public static NPCStrategyFactory getInstance() {
        return instance;
    }

    public NPCStrategy createStrategy(String NPCStrategyName, Player[] players) {
        // Modify this later when legal and smart are created.
        if(NPCStrategyName.equals("random")) {
            return new NPCRandomStrategy();
        } else if(NPCStrategyName.equals("legal")) {
            return new NPCLegalStrategy<>(players);
        } else if (NPCStrategyName.equals("smart")) {
            return new NPCSmartStrategy<>(players);
        } else {
           return null;
        }
    }
}
