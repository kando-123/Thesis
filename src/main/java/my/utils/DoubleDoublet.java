package my.utils;

/**
 *
 * @author Kay Jay O'Nail
 */
public class DoubleDoublet
{
    public double xCoord;
    public double yCoord;
    
    public DoubleDoublet(double x, double y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public DoubleDoublet clone()
    {
        return new DoubleDoublet(xCoord, yCoord);
    }
    
    public DoubleDoublet plus(DoubleDoublet other)
    {
        double x = xCoord + other.xCoord;
        double y = yCoord + other.yCoord;
        return new DoubleDoublet(x, y);
    }
    
    public DoubleDoublet minus(DoubleDoublet other)
    {
        double x = xCoord - other.xCoord;
        double y = yCoord - other.yCoord;
        return new DoubleDoublet(x, y);
    }
    
    public DoubleDoublet times(double factor)
    {
        double x = factor * xCoord;
        double y = factor * yCoord;
        return new DoubleDoublet(x, y);
    }
    
    public void add(DoubleDoublet other)
    {
        xCoord += other.xCoord;
        yCoord += other.yCoord;
    }
    
    public void subtract(DoubleDoublet other)
    {
        xCoord -= other.xCoord;
        yCoord -= other.yCoord;
    }
    
    public void multiply(double factor)
    {
        xCoord *= factor;
        yCoord *= factor;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other.getClass() == DoubleDoublet.class)
        {
            DoubleDoublet that = (DoubleDoublet) other;
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
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.xCoord) ^ (Double.doubleToLongBits(this.xCoord) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.yCoord) ^ (Double.doubleToLongBits(this.yCoord) >>> 32));
        return hash;
    }
}
