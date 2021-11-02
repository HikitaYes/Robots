package stateSaving;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.Map;

public class SaveableJInternalFrame extends JInternalFrame implements Saveable
{
    public SaveableJInternalFrame(String title, boolean resizable,
                                  boolean closeable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closeable, maximizable, iconifiable);
    }

    @Override
    public void saveState() {
        var state = new WindowState(
                getWidth(), getHeight(), getX(), getY(),
                isIcon(), isMaximum(), isClosed());
        DataStoring.store(getName(), state);
    }

    @Override
    public void loadState(Map<String, WindowState> data) {
        WindowState state = data.get(getName());
        setSize(state.getWidth(), state.getHeight());
        setLocation(state.getX(), state.getY());
        try {
            setIcon(state.isIcon());
            setMaximum(state.isMaximum());
            setClosed(state.isClosed());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
