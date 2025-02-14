package stateSaving;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataSaver {
    private static final Map<String, WindowState> data = new HashMap<>();

    public static void store(String nameWindow, WindowState state) {
        data.put(nameWindow, state);
    }

    private static final File file = new File(System.getProperty("user.home") + File.separator + "javaRobotsState");

    public static void save(Saveable[] windows) {
        for (var window : windows) {
            window.saveState();
        }
        saveToFile();
    }

    public static void load(Saveable[] windows) {
        Map<String, WindowState> data = loadFromFile();
        if (data != null) {
            for (var window : windows) {
                window.loadState(data);
            }
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            stream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, WindowState> loadFromFile() {
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
