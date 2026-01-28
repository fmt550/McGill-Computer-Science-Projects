public class SniperBee extends HoneyBee {

    private int attackDamage;
    private int piercingPower;
    public static int BASE_HEALTH;
    public static int BASE_COST;
    private boolean aiming;

    public SniperBee(Tile position, int attackDamage, int piercingPower) {
        super(position, BASE_HEALTH, BASE_COST);
        this.attackDamage = attackDamage;
        this.piercingPower = piercingPower;
        this.aiming = false;
    }

    public boolean takeAction() {
        if (getPosition() == null) return false;
        if (!getPosition().isOnThePath()) return false;

        if (!aiming) {
            aiming = true;
            return false;
        }

        aiming = false;
        Tile tile = getPosition().towardTheNest();

        while (tile != null) {
            if (tile.isNest()) return false;
            if (tile.getNumOfHornets() > 0) {
                Hornet[] swarm = tile.getHornets();
                int n = piercingPower < swarm.length ? piercingPower : swarm.length;
                for (int i = 0; i < n; i++) {
                    swarm[i].takeDamage(attackDamage);
                }
                return true;
            }
            tile = tile.towardTheNest();
        }
        return false;
    }
}
