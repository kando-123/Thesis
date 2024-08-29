package my.utils;

import java.util.Objects;

/**
 *
 * @author Kay Jay O'Nail
 */
public class IntegersDoublet
{
    public int xCoord;
    public int yCoord;
    
    public IntegersDoublet(int x, int y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public IntegersDoublet clone()
    {
        return new IntegersDoublet(xCoord, yCoord);
    }
    
    public IntegersDoublet plus(IntegersDoublet other)
    {
        int x = xCoord + other.xCoord;
        int y = yCoord + other.yCoord;
        return new IntegersDoublet(x, y);
    }
    
    public IntegersDoublet minus(IntegersDoublet other)
    {
        int x = xCoord - other.xCoord;
        int y = yCoord - other.yCoord;
        return new IntegersDoublet(x, y);
    }
    
    public IntegersDoublet times(int factor)
    {
        int x = factor * xCoord;
        int y = factor * yCoord;
        return new IntegersDoublet(x, y);
    }
    
    public void add(IntegersDoublet other)
    {
        xCoord += other.xCoord;
        yCoord += other.yCoord;
    }
    
    public void subtract(IntegersDoublet other)
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
        if (other.getClass() == IntegersDoublet.class)
        {
            IntegersDoublet that = (IntegersDoublet) other;
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
