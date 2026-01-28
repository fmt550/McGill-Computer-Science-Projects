public class BusyBee extends HoneyBee {

    public static int BASE_HEALTH;
    public static int BASE_COST;
    public static int BASE_AMOUNT_COLLECTED;


    public BusyBee (Tile position) {
        super(position, BASE_HEALTH, BASE_COST);
    }

    public boolean takeAction() {
        if (getPosition() == null) return false;
        getPosition().storeFood(BASE_AMOUNT_COLLECTED);
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BusyBee)) return false;
        return super.equals(obj);
    }
}
