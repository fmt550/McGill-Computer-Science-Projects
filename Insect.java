public abstract class Insect {

    private Tile position;
    private int health;


    public Insect (Tile position, int hp ){

        this.health = hp;


        if (position != null){
            boolean add = position.addInsect(this);
            this.position = position;

            }
        else{

            throw new IllegalArgumentException("Insect cannot be added, check type to ensure it can be added");
        }




    }

    public final Tile getPosition(){
        return this.position;
    }

    public final int getHealth(){
        return this.health;
    }

    public void setPosition(Tile newPosition){
        this.position = newPosition;
    }

    public void takeDamage(int damage) {

        this.health -= damage;
        if (this.health <= 0) {

            this.position.removeInsect(this);
        }

    }

    public void regenerateHealth(double percent) {
        int increase = (int)(this.health * (percent / 100.0));
        this.health += increase;
    }



    public abstract boolean takeAction();


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.getHealth() == ((Insect) obj).getHealth()
                && this.getPosition() == ((Insect) obj).getPosition();
    }
}

