/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ScreenPanel extends JPanel implements Runnable
{
    public final int hexOuterRadius;
    public final int hexInnerRadius;
    public final int colsCount;
    public final int rowsCount;
    public final int screenWidth;
    public final int screenHeight;

    private Thread gameThread;
    
    private World world;
    
    public ScreenPanel()
    {
        hexOuterRadius = 25;
        hexInnerRadius = (int) ((double) hexOuterRadius * Math.sin(Math.PI / 6.0));
        colsCount = 30;
        rowsCount = 30;
        screenWidth = colsCount * hexOuterRadius * 3 / 2;
        screenHeight = rowsCount * hexInnerRadius * 2;
        
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
        /* Update stuff. */
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        
        /* Draw stuff. */
        //world.draw(graphics2D);

        graphics2D.dispose();
    }
}
