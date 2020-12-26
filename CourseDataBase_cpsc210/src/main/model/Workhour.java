package model;



import java.util.HashMap;

public class Workhour {
    private static volatile Workhour instance;
    private HashMap<Stuff,Integer> workhourlist;

    private Workhour() {
        workhourlist = new HashMap<>();
    }

    //EFFECTS: Singleton pattern, only one instance was created.
    public static Workhour getInstance() {
        if (instance == null) {
            synchronized (Stufflist.class) {
                if (instance == null) {
                    instance = new Workhour();
                }
            }
        }
        return instance;
    }

    public Integer getWorkhour(Stuff stuff) {
        return workhourlist.get(stuff);
    }

    //MODIFIES: this
    //EFFECTS: put stuff along with its workhour
    public void addStuffhour(Stuff stuff, Integer workhour) {
        workhourlist.put(stuff,workhour);
    }

    //MODIFIES: nothing
    //EFFECTS: return true if the list contain the stuff with the given working hour.
    public  boolean contain(Integer workhour) {
        return workhourlist.containsValue(workhour);
    }

    //MODIFIES: this
    //EFFECTS: add the work hour to the given stuff.
    public void addWorkhour(Stuff stuff, Integer hourtoadd) {
        workhourlist.replace(stuff,getWorkhour(stuff) + hourtoadd);
    }
}
