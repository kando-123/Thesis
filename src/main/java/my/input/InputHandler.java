/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InputHandler implements KeyListener
{
    private int directions = 0;
    private boolean east;
    private boolean south;
    private boolean west;
    private boolean north;
    
    private boolean zoomIn;
    private boolean zoomOut;
    
    private InputHandler()
    {
        return;
    }
    
    private static InputHandler instance = null;
    
    public static InputHandler getInstance()
    {
        if (instance == null)
        {
            instance = new InputHandler();
        }
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        return;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch (code)
        {
            case KeyEvent.VK_RIGHT ->
            {
                //++directions;
                east = true;
            }
            case KeyEvent.VK_DOWN ->
            {
                //++directions;
                south = true;
            }
            case KeyEvent.VK_LEFT ->
            {
                //++directions;
                west = true;
            }
            case KeyEvent.VK_UP ->
            {
                //++directions;
                north = true;
            }
            case KeyEvent.VK_Q ->
            {
                System.out.println("plus");
                zoomIn = true;
            }
            case KeyEvent.VK_W ->
            {
                System.out.println("minus");
                zoomOut = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch (code)
        {
            case KeyEvent.VK_RIGHT ->
            {
                //--directions;
                east = false;
            }
            case KeyEvent.VK_DOWN ->
            {
                //--directions;
                south = false;
            }
            case KeyEvent.VK_LEFT ->
            {
                //--directions;
                west = false;
            }
            case KeyEvent.VK_UP ->
            {
                //--directions;
                north = false;
            }
        }
    }

    public boolean shiftEastwards()
    {
        return east /*&& directions == 1*/;
    }

    public boolean shiftSouthwards()
    {
        return south /*&& directions == 1*/;
    }

    public boolean shiftWestwards()
    {
        return west /*&& directions == 1*/;
    }

    public boolean shiftNorthwards()
    {
        return north /*&& directions == 1*/;
    }
    
    public boolean zoomIn()
    {
        boolean value = zoomIn;
        zoomIn = false;
        return value;
    }
    
    public boolean zoomOut()
    {
        boolean value = zoomOut;
        zoomOut = false;
        return value;
    }
}
