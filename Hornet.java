public class Hornet extends Insect {

    private int attackDamage;
    public static int BASE_FIRE_DMG;
    private boolean isTheQueen = false;
    public static int numOfQueens = 0;


    public Hornet(Tile position, int hp, int attackDamage) {
        super(position, hp);
        this.attackDamage = attackDamage;
    }


    public Hornet(Tile position, int hp) {
        super(position, hp);
        this.attackDamage = 0;
    }


    public boolean isTheQueen() {
        return this.isTheQueen;
    }

    public void promote() {
        if (numOfQueens == 0) {
            this.isTheQueen = true;
            numOfQueens++;
        }
    }

    public boolean takeAction() {
        int actions;
        if (isTheQueen) {
            actions = 2;
        } else {
            actions = 1;
        }

        boolean didSomething = false;

        for (int i = 0; i < actions; i++) {
            if (getPosition() == null) return didSomething;

            if (getPosition().isOnFire()) {
                takeDamage(BASE_FIRE_DMG);
                if (getPosition() == null) return didSomething;
            }

            if (getPosition().getBee() != null) {
                getPosition().getBee().takeDamage(attackDamage);
                didSomething = true;
                continue;
            }

            if (getPosition().isHive()) {
                continue;
            }

            Tile next = getPosition().towardTheHive();
            if (next != null) {
                getPosition().removeInsect(this);
                next.addInsect(this);
                didSomething = true;
            }
        }

        return didSomething;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Hornet)) return false;
        if (!super.equals(obj)) return false;
        return this.attackDamage == ((Hornet) obj).attackDamage;
    }
}
