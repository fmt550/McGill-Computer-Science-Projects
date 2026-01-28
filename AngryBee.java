public class AngryBee extends HoneyBee {

    private int attackDamage;
    public static int BASE_HEALTH;
    public static int BASE_COST;

    public AngryBee(Tile position, int attackDamage){
        super(position,BASE_HEALTH,BASE_COST );
        this.attackDamage = attackDamage;
    }

    public boolean takeAction() {
        if (getPosition() == null) return false;
        if (!getPosition().isOnThePath()) return false;

        if (getPosition().getHornet() != null) {
            getPosition().getHornet().takeDamage(attackDamage);
            return true;
        }

        if (getPosition().towardTheNest() == null) return false;
        if (getPosition().towardTheNest().isNest()) return false;

        if (getPosition().towardTheNest().getHornet() != null) {
            getPosition().towardTheNest().getHornet().takeDamage(attackDamage);
            return true;
        }

        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AngryBee)) return false;
        if (!super.equals(obj)) return false;
        return this.attackDamage == ((AngryBee) obj).attackDamage;
    }

}
