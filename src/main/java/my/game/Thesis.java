/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import javax.swing.*;
import my.input.InputHandler;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Thesis
{
    public static void main(String[] args)
    {        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("The Global Empire");
        
        ScreenPanel panel = new ScreenPanel();
        frame.add(panel);
        frame.pack();
        
        InputHandler inputHandler = InputHandler.getInstance();
        frame.addKeyListener(inputHandler);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        panel.startGameThread();
    }
}
