package oh_heaven.game;

public class NPCStrategyFactory {
    private static NPCStrategyFactory instance = new NPCStrategyFactory();

    private NPCStrategyFactory() {};

    public static NPCStrategyFactory getInstance() {
        return instance;
    }

    public NPCStrategy createStrategy(String NPCStrategyName, Player[] players) {
        // Creates the appropriate strategy according to the name and forwards the Player array so the strategies
        // can subscribe and observe to other players to get the information they need instead of accessing a shared
        // pool as per the requirements of NERDI.
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
