package ge.main;

import ge.gui.PlayerConfigContentPane;
import javax.swing.JFrame;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Engine
{
    public static void main(String[] args)
    {
        var frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");
        frame.setResizable(false);

        var contentPane = new PlayerConfigContentPane(null);
        frame.setContentPane(contentPane);
        frame.pack();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
