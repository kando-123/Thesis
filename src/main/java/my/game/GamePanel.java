/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.*;
import javax.swing.*;

/**
 * 
 * @author Kay Jay O'Nail
 */
public class GamePanel extends JPanel implements Runnable
{
    private Thread gameThread;
    
    public GamePanel()
    {
        setPreferredSize(new Dimension(300, 200));
    }
    
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    private final int NANOSECONDS_PER_SECOND = 1_000_000_000;
    private final int fps = 60;
    
    @Override
    public void run()
    {
        long period = (long) ((double) NANOSECONDS_PER_SECOND / (double) fps);
        long recentTime = System.nanoTime();
        while (gameThread != null)
        {
            long currentTime = System.nanoTime();
            if (currentTime - recentTime >= period)
            {
                /* UPDATE */
                update();

                /* RENDER */
                repaint();

                /* Reset the counter */
                recentTime = currentTime;
            }
        }
    }
    
    private void update()
    {
        /* Update stuff. */
    }
    
    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        /* Draw all. */
        
        graphics2D.dispose();
    }
}
