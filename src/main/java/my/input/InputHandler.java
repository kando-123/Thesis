package my.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import my.world.OrthogonalDirection;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InputHandler implements KeyListener, MouseWheelListener
{
    private boolean control;
    
    private MovementMode movementMode;
    private int directions;
    private boolean east;
    private boolean south;
    private boolean west;
    private boolean north;
    
    private boolean zoomIn;
    private boolean zoomOut;
    
    private InputHandler()
    {
        movementMode = MovementMode.MOVE_CAMERA;
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
            
            case KeyEvent.VK_M ->
            {
                if (control)
                {
                    if (movementMode == MovementMode.MOVE_CAMERA)
                    {
                        movementMode = MovementMode.MOVE_WORLD;
                    }
                    else
                    {
                        movementMode = MovementMode.MOVE_CAMERA;
                    }
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
            
            case KeyEvent.VK_CONTROL ->
            {
                control = false;
            }
        }
    }
    
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        System.out.println(e.paramString());
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
        
        if (direction != null && movementMode == MovementMode.MOVE_WORLD)
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
