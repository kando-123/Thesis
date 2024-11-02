package ge.view;

import ge.utilities.*;
import java.awt.event.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InputHandler implements KeyListener
{
    private boolean control;
    
    private boolean moveCamera;
    
    private boolean east;
    private boolean south;
    private boolean west;
    private boolean north;
    
    private boolean zoomIn;
    private boolean zoomOut;
    
    InputHandler()
    {
        moveCamera = true;
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
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
            {
                east = true;
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
            {
                south = true;
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
            {
                west = true;
            }
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
            {
                north = true;
            }
            
            case KeyEvent.VK_E, KeyEvent.VK_ADD ->
            {
                zoomIn = true;
            }
            case KeyEvent.VK_Q, KeyEvent.VK_SUBTRACT ->
            {
                zoomOut = true;
            }
            case KeyEvent.VK_SPACE ->
            {
                east = south = west = north = zoomIn = zoomOut = false;
            }
            
            case KeyEvent.VK_M ->
            {
                if (control)
                {
                    moveCamera = !moveCamera;
                }
            }
            
            case KeyEvent.VK_CONTROL ->
            {
                control = true;
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch (code)
        {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
            {
                east = false;
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
            {
                south = false;
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
            {
                west = false;
            }
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
            {
                north = false;
            }
            case KeyEvent.VK_E, KeyEvent.VK_ADD ->
            {
                zoomIn = false;
            }
            case KeyEvent.VK_Q, KeyEvent.VK_SUBTRACT ->
            {
                zoomOut = false;
            }
            
            case KeyEvent.VK_CONTROL ->
            {
                control = false;
            }
        }
    }
    
    public OrthogonalDirection getShiftingDirection()
    {
        OrthogonalDirection direction = null;
        
        if (east && !west && south == north)
        {
            direction = OrthogonalDirection.EAST;
        }
        else if (south && !north && west == east)
        {
            direction = OrthogonalDirection.SOUTH;
        }
        else if (west && !east && north == south)
        {
            direction = OrthogonalDirection.WEST;
        }
        else if (north && !south && east == west)
        {
            direction = OrthogonalDirection.NORTH;
        }
        else if (east && south && !west && !north)
        {
            direction = OrthogonalDirection.SOUTHEAST;
        }
        else if (south && west && !north && !east)
        {
            direction = OrthogonalDirection.SOUTHWEST;
        }
        else if (west && north && !east && !south)
        {
            direction = OrthogonalDirection.NORTHWEST;
        }
        else if (north && east && !south && ! west)
        {
            direction = OrthogonalDirection.NORTHEAST;
        }
        
        if (direction != null && !moveCamera)
        {
            direction = direction.opposite();
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
