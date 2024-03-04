/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.game;

import javax.swing.*;

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
        frame.setResizable(false);
        frame.setTitle("The Global Empire");
        
        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.pack();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
