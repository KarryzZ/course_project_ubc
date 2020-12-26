package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Filemanager {

    //EFFECTS: return the saved courses File.
    public static File coursefile() {
        return new File("./data/Courses_save.txt");
    }


    //EFFECTS: return the saved stuff File.
    public static File stufffile() {
        return new File("./data/Stuff_save.txt");
    }

    //EFFECTS: read the txt coursefile to String
    public static String getcoursetxtfile() throws IOException {
        List<String> linesofcourse = Files.readAllLines(coursefile().toPath());
        StringBuilder sb = new StringBuilder();
        for (String s : linesofcourse) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}
