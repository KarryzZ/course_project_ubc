package model;

import model.interfaces.Loadable;
import model.interfaces.Savable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stufflist implements Loadable, Savable,Iterable<Stuff> {
    private List<Stuff> stufflist;

    public Stufflist() {
        stufflist = new ArrayList<>();
    }

    public boolean contain(Stuff stuff) {
        return stufflist.contains(stuff);
    }

    //REQUIRES: stufflist not empty.
    //EFFECTS: Return ture if the stuff list contain the stuff with the given id, false otherwise.
    public boolean contain(Integer id) {
        for (Stuff stuff : stufflist) {
            if (stuff.getStuffid() == id) {
                return true;
            }
        }
        return false;
    }

    public void add(Stuff s) {
        stufflist.add(s);
    }

    public Stuff get(int i) {
        return stufflist.get(i);
    }

    public int size() {
        return stufflist.size();
    }

    public boolean isCorrect(int id, String password) {
        return stufflist.contains(new Stuff(id,password));
    }

    //REQUIRES: stufflist not empty
    //MODIFIES:
    //EFFECTS: return the stuff with given id and password.
    public Stuff getStuff(int id, String password) {
        for (Stuff s: stufflist) {
            if (s.equals(new Stuff(id,password))) {
                return s;
            }
        }
        return null;
    }


    //MODIFIES: File f
    //EFFECTS: save the stuff information to the given file.
    @Override
    public void save(File f) throws IOException {
        List<String> linesofitem = new ArrayList<>();
        for (Stuff s : stufflist) {
            linesofitem.add(s.getStuffid() + " " + s.getPassword());
            Files.write(f.toPath(), linesofitem);
        }
    }

    //MODIFIES: this
    //EFFECTS: load all the courses in the given text file.
    @Override
    public void load(File f) throws IOException {
        List<String> linesofstuff = Files.readAllLines(f.toPath());
        for (String s : linesofstuff) {
            String[] input = s.split(",");
            Stuff stuff = new Stuff(Integer.parseInt(input[0]), input[1]);
            stufflist.add(stuff);
        }
    }

    //EFFECTS: return the iterator for stufflist.
    @Override
    public Iterator<Stuff> iterator() {
        return stufflist.iterator();
    }
}
