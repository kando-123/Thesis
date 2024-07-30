package my.utils;

import java.util.Objects;

/**
 *
 * @author Kay Jay O'Nail
 */
public class IntegerDoublet
{
    public int xCoord;
    public int yCoord;
    
    public IntegerDoublet(int x, int y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public IntegerDoublet clone()
    {
        return new IntegerDoublet(xCoord, yCoord);
    }
    
    public IntegerDoublet plus(IntegerDoublet other)
    {
        int x = xCoord + other.xCoord;
        int y = yCoord + other.yCoord;
        return new IntegerDoublet(x, y);
    }
    
    public IntegerDoublet minus(IntegerDoublet other)
    {
        int x = xCoord - other.xCoord;
        int y = yCoord - other.yCoord;
        return new IntegerDoublet(x, y);
    }
    
    public IntegerDoublet times(int factor)
    {
        int x = factor * xCoord;
        int y = factor * yCoord;
        return new IntegerDoublet(x, y);
    }
    
    public void add(IntegerDoublet other)
    {
        xCoord += other.xCoord;
        yCoord += other.yCoord;
    }
    
    public void subtract(IntegerDoublet other)
    {
        xCoord -= other.xCoord;
        yCoord -= other.yCoord;
    }
    
    public void multiply(int factor)
    {
        xCoord *= factor;
        yCoord *= factor;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other.getClass() == IntegerDoublet.class)
        {
            IntegerDoublet that = (IntegerDoublet) other;
            return this.xCoord == that.xCoord && this.yCoord == that.yCoord;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.xCoord);
        hash = 89 * hash + Objects.hashCode(this.yCoord);
        return hash;
    }
}
