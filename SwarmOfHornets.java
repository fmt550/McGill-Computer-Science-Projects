public class SwarmOfHornets {

    private Hornet[] hornets;
    private int numOfHornets;
    public static double QUEEN_BOOST;

    public SwarmOfHornets(){

        this.hornets = new Hornet[0];
        this.numOfHornets = 0;

    }

    public int sizeOfSwarm(){
        return numOfHornets;
    }

    public Hornet[] getHornets(){

        Hornet[] allHornets = new Hornet[numOfHornets];

        for (int i=0;i<numOfHornets;i++) {

            if (hornets[i] != null) {

                allHornets[i] = hornets[i];

            }

        }
        return allHornets;
    }

    public Hornet getFirstHornet() {

        if (numOfHornets == 0) {

            return null;
        } else {
            return hornets[0];

        }
    }
    public void addHornet(Hornet hornet) {
        if (hornet == null) return;

        Hornet[] biggerHornets = new Hornet[numOfHornets + 1];


        for (int i = 0; i < numOfHornets; i++) {
            biggerHornets[i] = hornets[i];
        }
        if (Hornet.numOfQueens == 0) {
            hornet.promote();
        }

        if (hornet.isTheQueen()) {
            for (int i = 0; i < numOfHornets; i++) {
                if (hornets[i] != hornet) {
                    hornets[i].regenerateHealth(QUEEN_BOOST);
                }
            }
        }

        biggerHornets[numOfHornets] = hornet;

        hornets = biggerHornets;
        numOfHornets++;
    }

    public boolean removeHornet(Hornet hornet) {
        for (int i = 0; i < numOfHornets; i++) {
            if (hornets[i] == hornet) {

                for (int j = i + 1; j < numOfHornets; j++) {
                    hornets[j - 1] = hornets[j];
                }
                numOfHornets--;
                hornets[numOfHornets] = null;
                return true;
            }
        }
        return false;
    }
}










