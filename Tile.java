public class Tile {

    private int food;
    private boolean isHive;
    private boolean isNest;
    private boolean onThePath;
    private Tile towardTheHive;
    private Tile towardTheNest;
    private HoneyBee bee;
    private SwarmOfHornets swarm;
    private boolean onFire;

    public Tile() {
        food = 0;
        isHive = false;
        isNest = false;
        onThePath = false;
        towardTheHive = null;
        towardTheNest = null;
        bee = null;
        swarm = null;

    }

    public Tile(int food, boolean isHive, boolean isNest, boolean onThePath, Tile towardTheHive,
                Tile towardTheNest, HoneyBee bee, SwarmOfHornets swarm) {
        this.food = food;
        this.isHive = isHive;
        this.isNest = isNest;
        this.onThePath = onThePath;
        this.towardTheHive = towardTheHive;
        this.towardTheNest = towardTheNest;
        this.bee = bee;
        this.swarm = swarm;
    }

    public boolean isHive() {
        return isHive;
    }

    public boolean isNest() {
        return isNest;
    }

    public void buildHive() {
        this.isHive = true;

    }

    public void buildNest() {
        this.isNest = true;
    }

    public boolean isOnThePath() {

        return this.onThePath;
    }

    public Tile towardTheHive() {
        if (this.onThePath && !this.isHive) {
            return towardTheHive;
        } else {

            return null;
        }
    }

    public Tile towardTheNest() {
        if (this.onThePath && !this.isNest) {
            return towardTheNest;
        } else {
            return null;
        }
    }

    public void createPath(Tile nextTowardTheHive, Tile nextTowardTheNest) {


        if (nextTowardTheHive == null && !this.isHive || nextTowardTheNest == null && !this.isNest) {
            throw new IllegalArgumentException("Tile can only be null if outer bounds or if hive/nest already on tile");
        }
        this.towardTheHive = nextTowardTheHive;
        this.towardTheNest = nextTowardTheNest;
        this.onThePath = true;
    }

    public int collectFood() {
        int collectedFood = this.food;
        this.food = 0;
        return collectedFood;

    }

    public void storeFood(int food) {

        this.food += food;

    }

    public int getNumOfHornets() {
        return this.swarm.sizeOfSwarm();
    }

    public HoneyBee getBee() {
        return this.bee;
    }

    public Hornet getHornet() {
        return this.swarm.getFirstHornet();
    }

    public Hornet[] getHornets() {
        return this.swarm.getHornets();
    }

    public boolean addInsect(Insect insect) {
        if (insect instanceof HoneyBee) {

            if (this.bee == null && !this.isNest) {
                this.bee = (HoneyBee) insect;
                insect.setPosition(this);
                return true;
            }
        } else if (insect instanceof Hornet) {

            if (this.onThePath) {
                if (this.swarm == null) {
                    this.swarm = new SwarmOfHornets();
                }
                this.swarm.addHornet((Hornet) insect);
                insect.setPosition(this);
                return true;
            }
        }
        return false;
    }

    public boolean removeInsect(Insect insect) {
        if (insect instanceof HoneyBee) {

            if (this.bee == insect) {
                this.bee = null;
                insect.setPosition(null);
                return true;
            }
            return false;
        } else if (insect instanceof Hornet) {

            boolean removed = this.swarm.removeHornet((Hornet) insect);
            if (removed) {
                insect.setPosition(null);
                return true;
            }
            return false;
        }

        return false;
    }

    public void setOnFire() {
        this.onFire = true;
    }

    public boolean isOnFire() {
        return this.onFire;
    }
}








