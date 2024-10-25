package ge.gui;

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
    
    private int directions;
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
                if (!east)
                { 
                    ++directions;
                }
                east = true;
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
            {
                if (!south)
                {
                    ++directions;
                }
                south = true;
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
            {
                if (!west)
                {
                    ++directions;
                }
                west = true;
            }
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
            {
                if (!north)
                {
                    ++directions;
                }
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
                --directions;
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
            {
                south = false;
                --directions;
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
            {
                west = false;
                --directions;
            }
            case KeyEvent.VK_UP, KeyEvent.VK_W ->
            {
                north = false;
                --directions;
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
