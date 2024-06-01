package my.world;

import java.util.Objects;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Pixel
{
    public int xCoord;
    public int yCoord;
    
    public Pixel(int x, int y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public Pixel clone()
    {
        return new Pixel(xCoord, yCoord);
    }
    
    public Pixel plus(Pixel other)
    {
        int x = xCoord + other.xCoord;
        int y = yCoord + other.yCoord;
        return new Pixel(x, y);
    }
    
    public Pixel minus(Pixel other)
    {
        int x = xCoord - other.xCoord;
        int y = yCoord - other.yCoord;
        return new Pixel(x, y);
    }
    
    public Pixel times(int factor)
    {
        int x = factor * xCoord;
        int y = factor * yCoord;
        return new Pixel(x, y);
    }
    
    public void add(Pixel other)
    {
        xCoord += other.xCoord;
        yCoord += other.yCoord;
    }
    
    public void subtract(Pixel other)
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
        if (other.getClass() == Pixel.class)
        {
            Pixel that = (Pixel) other;
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
