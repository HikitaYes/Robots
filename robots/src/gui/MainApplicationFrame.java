package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Logger;
import stateSaving.DataSaver;
import stateSaving.Saveable;
import stateSaving.SaveableJFrame;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends SaveableJFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final Saveable[] windowsToSave;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset * 2,
            screenSize.height - inset * 2);

        setContentPane(desktopPane);
        setName("main");
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        pack();
        setExtendedState(MAXIMIZED_BOTH);
        windowsToSave = new Saveable[] {logWindow, gameWindow, this};
        DataSaver.load(windowsToSave);

        addCloseEventHandler();
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
//        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu(
            "Режим отображения",
            KeyEvent.VK_V,
            "Управление режимом отображения приложения",
            new JMenuItem[] {
                createMenuItem("Системная схема", (event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                }),
                createMenuItem("Универсальная схема", (event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                })
            }
        );

        JMenu testMenu = createMenu(
            "Тесты",
            KeyEvent.VK_T,
            "Тестовые команды",
            new JMenuItem[] {
                createMenuItem("Сообщение в лог", (event) -> {
                    Logger.debug("Новая строка");
                })
            }
        );

        JMenu fileMenu = createMenu(
            "Файл",
            KeyEvent.VK_F,
            "Выход из приложения",
            new JMenuItem[] {
                createMenuItem("Выход", (event) -> {
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                })
            }
        );

        JMenu[] menu = {fileMenu, lookAndFeelMenu, testMenu};
        for (JMenu item : menu) {
            menuBar.add(item);
        }
        return menuBar;
    }

    private JMenuItem createMenuItem(String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name, KeyEvent.VK_S);
        item.addActionListener(listener);
        return item;
    }

    private JMenu createMenu(String name, int key, String description, JMenuItem[] menuItems) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(description);
        for (JMenuItem item : menuItems) {
            menu.add(item);
        }
        return menu;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private void addCloseEventHandler() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        e.getWindow(),
                        "Вы уверены, что хотите выйти?",
                        "Окно подтверждения",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    DataSaver.save(windowsToSave);
                    e.getWindow().setVisible(false);
                    System.exit(0);
                }
            }
        });
    }
}
