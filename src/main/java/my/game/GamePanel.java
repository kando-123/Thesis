/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import my.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class GamePanel extends JPanel implements Runnable, ComponentListener, MouseListener
{
    public int panelWidth;
    public int panelHeight;

    private Thread gameThread;
    
    private final WorldPanel worldPanel;
    
    public GamePanel(Dimension panelSize)
    {
        super(new BorderLayout());
        
        panelWidth = panelSize.width;
        panelHeight = panelSize.height;
        
        JPanel north = new JPanel();
        JPanel east = new JPanel();
        JPanel south = new JPanel();
        JPanel west = new JPanel();
        //JPanel center = new JPanel();
        north.add(new JLabel("North"));
        east.add(new JLabel("East"));
        south.add(new JLabel("South"));
        west.add(new JLabel("West"));
        
        Dimension horizontal = new Dimension(panelWidth, panelHeight / 20);
        Dimension vertical = new Dimension(panelWidth / 20, panelHeight);
        north.setPreferredSize(horizontal);
        south.setPreferredSize(horizontal);
        east.setPreferredSize(vertical);
        west.setPreferredSize(vertical);
        
        int worldWidth = panelSize.width - 2 * vertical.width;
        int worldHeight = panelSize.height - 2 * horizontal.height;
        Dimension worldPanelSize = new Dimension(worldWidth, worldHeight);
        
        WorldParameters worldParameters = new WorldParameters();
        worldParameters.worldSide = 20;
        worldParameters.seaPercentage = 0.40;
        worldParameters.mountainsOnLandPercentage = 0.30;
        worldParameters.woodsOnLandPercentage = 0.20;
        
        worldPanel = new WorldPanel();
        worldPanel.makeWorld(worldPanelSize, worldParameters);
        
        add(worldPanel, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);
        add(east, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
        add(west, BorderLayout.WEST);
        
        setPreferredSize(new Dimension(panelWidth, panelHeight));
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
        worldPanel.update();
    }
    
    @Override
    public void componentResized(ComponentEvent e)
    {
        /* To be implemented... */
        System.out.println("The frame has been resized.");
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
        return;
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
        return;
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
        return;
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        return;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        return;
    }
}
