package ge.gui;

import ge.manager.*;
import ge.utilities.*;
import java.awt.HeadlessException;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GUI extends JFrame implements ActionListener
{
    private final JFrame frame;
    
    private Invoker<WorldManager> worldInvoker;
    private Invoker<PlayerManager> playerInvoker;
    
    private UserPanel userPanel;
    private WorldPanel worldPanel;

    public GUI(JFrame frame) throws HeadlessException
    {
        this.frame = frame; 
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
