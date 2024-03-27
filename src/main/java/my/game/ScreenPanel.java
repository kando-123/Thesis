/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.*;
import javax.swing.*;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ScreenPanel extends JPanel implements Runnable
{
    public final int colsCount;
    public final int rowsCount;
    public final int screenWidth;
    public final int screenHeight;

    private Thread gameThread;
    
    private final World world;
    
    public ScreenPanel()
    {
        world = new World(10);
        
        colsCount = 30;
        rowsCount = 20;
        screenWidth = (int) (((double) colsCount * 0.75 + 0.25) * (double) world.hexWidth);
        screenHeight = rowsCount * world.hexHeight;
        
        world.setCenter(new Pixel(screenWidth / 2, screenHeight / 2));
        
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private final int NANOSECONDS_PER_SECOND = 1_000_000_000;
    private final int FPS = 60;
    private final long PERIOD = (long) (NANOSECONDS_PER_SECOND / FPS);

    @Override
    public void run()
    {
        long recentTime = System.nanoTime();
        while (gameThread != null)
        {
            long currentTime = System.nanoTime();
            if (currentTime - recentTime >= PERIOD)
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
        world.update();
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        
        /* Draw stuff. */
        world.draw(graphics2D);

        graphics2D.dispose();
    }
}
