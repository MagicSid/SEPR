package location;

/**
 * Allows the creation of a College object, which stores whether a particular college has been beaten
 * Had to implement Serializable due to encoding needed for saving game data.
 */
public class Storm implements java.io.Serializable {
    String name;
    boolean pain;
    int damage;
    public Storm(String name) {
        this.name = name;
        this.pain = false;
        
    }
    public Storm(String name,int damage) {
        this.name = name;
        this.pain = true;
        this.damage =damage;
    }

    public String getName() {
        return name;
    }

    public boolean ispainful() {
        return pain;
    }
    public int howpainful() {
    	return damage;
    }

    public static Storm Stormwarning = new Storm("stormwarning");
    public static Storm Stormpain = new Storm("stormpain",1);

}
