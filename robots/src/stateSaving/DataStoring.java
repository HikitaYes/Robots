package stateSaving;

import java.util.HashMap;
import java.util.Map;

public class DataStoring {
    private static final Map<String, WindowState> data = new HashMap<>();

    public static Map<String, WindowState> getData() {
        return data;
    }

    public static void store(String nameWindow, WindowState state) {
        data.put(nameWindow, state);
    }
}
