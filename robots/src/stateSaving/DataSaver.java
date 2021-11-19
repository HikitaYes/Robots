package stateSaving;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataSaver {
    private static final File file = new File(System.getProperty("user.home") + File.separator + "javaRobotsState");

    public static void save(Saveable[] windows) {
        for (var window : windows) {
            window.saveState();
        }
        saveToFile(DataStoring.getData());
    }

    public static void load(Saveable[] windows) {
        Map<String, WindowState> data = loadFromFile();
        if (data != null) {
            for (var window : windows) {
                window.loadState(data);
            }
        }
    }

    public static void saveToFile(Map<String, WindowState> data) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            stream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, WindowState> loadFromFile() {
        if (file.exists()) {
            try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                Map<String, WindowState> data = (HashMap<String, WindowState>) stream.readObject();
                return data;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
