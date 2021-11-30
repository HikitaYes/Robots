package gui;

import stateSaving.SaveableJInternalFrame;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends SaveableJInternalFrame
{
    private final GameVisualizer m_visualizer;
    public GameWindow(GameModel model)
    {
        super("Игровое поле", true, true, true, true);
        setName("model");
        m_visualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400,  400);
    }
}
