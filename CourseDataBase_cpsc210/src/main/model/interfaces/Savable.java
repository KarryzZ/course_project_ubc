package model.interfaces;

import java.io.File;
import java.io.IOException;

public interface Savable {
    void save(File f) throws IOException;
}
