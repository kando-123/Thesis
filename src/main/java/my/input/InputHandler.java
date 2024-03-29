/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import my.world.OrthogonalDirection;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InputHandler implements KeyListener
{
    private int directions;
    private boolean east;
    private boolean south;
    private boolean west;
    private boolean north;
    
    private boolean zoomIn;
    private boolean zoomOut;
    
    private InputHandler()
    {
        directions = 0;
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
                if (!east)
                {
                    ++directions;
                }
                east = true;
            }
            case KeyEvent.VK_DOWN ->
            {
                if (!south)
                {
                    ++directions;
                }
                south = true;
            }
            case KeyEvent.VK_LEFT ->
            {
                if (!west)
                {
                    ++directions;
                }
                west = true;
            }
            case KeyEvent.VK_UP ->
            {
                if (!north)
                {
                    ++directions;
                }
                north = true;
            }
            
            case KeyEvent.VK_CLOSE_BRACKET ->
            {
                zoomIn = true;
            }
            case KeyEvent.VK_OPEN_BRACKET ->
            {
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
                east = false;
                --directions;
            }
            case KeyEvent.VK_DOWN ->
            {
                south = false;
                --directions;
            }
            case KeyEvent.VK_LEFT ->
            {
                west = false;
                --directions;
            }
            case KeyEvent.VK_UP ->
            {
                north = false;
                --directions;
            }
            case KeyEvent.VK_CLOSE_BRACKET ->
            {
                zoomIn = false;
            }
            case KeyEvent.VK_OPEN_BRACKET ->
            {
                zoomOut = false;
            }
        }
    }
    
    public OrthogonalDirection getShiftingDirection()
    {
        OrthogonalDirection direction = null;
        
        if (directions == 1)
        {
            if (east)
            {
                direction = OrthogonalDirection.EAST;
            }
            else if (south)
            {
                direction = OrthogonalDirection.SOUTH;
            }
            else if (west)
            {
                direction = OrthogonalDirection.WEST;
            }
            else if (north)
            {
                direction = OrthogonalDirection.NORTH;
            }
        }
        else if (directions == 2)
        {
            if (south)
            {
                if (east)
                {
                    direction = OrthogonalDirection.SOUTHEAST;
                }
                else if (west)
                {
                    direction = OrthogonalDirection.SOUTHWEST;
                }
            }
            else if (north)
            {
                if (west)
                {
                    direction = OrthogonalDirection.NORTHWEST;
                }
                else if (east)
                {
                    direction = OrthogonalDirection.NORTHEAST;
                }
            }
        }
        
        return direction;
    }
    
    public boolean zoomIn()
    {
        return zoomIn;
    }
    
    public boolean zoomOut()
    {
        return zoomOut;
    }
}
