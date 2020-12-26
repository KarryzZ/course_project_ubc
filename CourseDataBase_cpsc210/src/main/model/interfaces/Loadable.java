package model.interfaces;

import java.io.File;
import java.io.IOException;

public interface Loadable {
    void load(File f) throws IOException;
}
