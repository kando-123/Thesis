 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import java.awt.*;
import javax.swing.*;
import my.input.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Master extends JFrame
{
    private Master()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("The Global Empire");
        setResizable(false);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int frameWidth = (int) ((double) screenSize.width / Math.sqrt(2.0));
        int frameHeight = (int) ((double) screenSize.height / Math.sqrt(2.0));
        Dimension frameSize = new Dimension(frameWidth, frameHeight);
        setSize(frameSize);
        
        GamePanel panel = new GamePanel(frameSize);
        setContentPane(panel);
        addComponentListener(panel);
        pack();
        
        InputHandler inputHandler = InputHandler.getInstance();
        addKeyListener(inputHandler);
        
        setVisible(true);
        setLocationRelativeTo(null);
        
        panel.startGameThread();
    }
    
    private static Master instance = null;
    
    public static Master getInstance()
    {
        if (instance == null)
        {
            instance = new Master();
        }
        return instance;
    }
}
