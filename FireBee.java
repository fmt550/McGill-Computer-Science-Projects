public class FireBee extends HoneyBee {

    private int attackDamage;
    public static int BASE_HEALTH;
    public static int BASE_COST;

    public FireBee(Tile position, int attackDamage) {
        super(position, BASE_HEALTH, BASE_COST);
        this.attackDamage = attackDamage;
    }

    public boolean takeAction() {
        if (getPosition() == null) return false;
        if (!getPosition().isOnThePath()) return false;

        // Sting the first hornet on the same tile
        Hornet target = getPosition().getHornet();
        if (target != null) {
            target.takeDamage(attackDamage);
            return true;
        }
        return false;
    }

    // Called by the game engine when this bee dies
    public void explode() {
        if (getPosition() == null) return;
        Hornet[] hornets = getPosition().getHornets();
        for (int i = 0; i < hornets.length; i++) {
            hornets[i].takeDamage(attackDamage);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FireBee)) return false;
        if (!super.equals(obj)) return false;
        return this.attackDamage == ((FireBee) obj).attackDamage;
    }
}
