package my.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Kay Jay O'Nail
 */
public interface DefaultWindowListener extends WindowListener
{
    @Override
    public default void windowActivated(WindowEvent e) {}

    @Override
    public default void windowClosed(WindowEvent e) {}

    @Override
    public default void windowClosing(WindowEvent e) {}

    @Override
    public default void windowDeactivated(WindowEvent e) {}

    @Override
    public default void windowDeiconified(WindowEvent e) {}

    @Override
    public default void windowIconified(WindowEvent e) {}

    @Override
    public default void windowOpened(WindowEvent e) {}
}
