public abstract class HoneyBee extends Insect {

    private int cost;
    public static double HIVE_DMG_REDUCTION;

    public HoneyBee (Tile position, int hp, int cost){
        super(position,hp);
        this.cost = cost;

    }

    public int getCost() {
        return cost;
    }

    public void takeDamage(int dmg) {
        int applied = dmg;
        if (getPosition() != null && getPosition().isHive()) {
            applied = (int) (dmg * (1.0 - HIVE_DMG_REDUCTION));
        }
        super.takeDamage(applied);
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof HoneyBee)) return false;
        if (!super.equals(obj)) return false;
        return this.getCost() == ((HoneyBee) obj).getCost();
    }
}
