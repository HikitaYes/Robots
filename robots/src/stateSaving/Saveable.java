package stateSaving;

import java.util.Map;

public interface Saveable {
    void saveState();

    void loadState(Map<String, WindowState> data);
}
