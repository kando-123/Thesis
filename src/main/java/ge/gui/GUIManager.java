package ge.gui;

import ge.utilities.Invoker;
import javax.swing.JFrame;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GUIManager
{
    private final JFrame frame;
    
    public GUIManager()
    {
        frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");
        frame.setResizable(false);

        var contentPane = new PlayerConfigContentPane(new Invoker<>(this));
        frame.setContentPane(contentPane);
        frame.pack();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    
    void beginPlayerConfig()
    {
        System.out.println("Beginning player configuration.");
    }
    
    void beginWorldConfig()
    {
        System.out.println("Beginning world configuration.");
    }
    
    void beginGameplay()
    {
        System.out.println("Beginning gameplay.");
    }
}
