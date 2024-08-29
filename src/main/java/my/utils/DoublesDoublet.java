package my.utils;

/**
 *
 * @author Kay Jay O'Nail
 */
public class DoublesDoublet
{
    public double xCoord;
    public double yCoord;
    
    public DoublesDoublet(double x, double y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public DoublesDoublet clone()
    {
        return new DoublesDoublet(xCoord, yCoord);
    }
    
    public DoublesDoublet plus(DoublesDoublet other)
    {
        double x = xCoord + other.xCoord;
        double y = yCoord + other.yCoord;
        return new DoublesDoublet(x, y);
    }
    
    public DoublesDoublet minus(DoublesDoublet other)
    {
        double x = xCoord - other.xCoord;
        double y = yCoord - other.yCoord;
        return new DoublesDoublet(x, y);
    }
    
    public DoublesDoublet times(double factor)
    {
        double x = factor * xCoord;
        double y = factor * yCoord;
        return new DoublesDoublet(x, y);
    }
    
    public void add(DoublesDoublet other)
    {
        xCoord += other.xCoord;
        yCoord += other.yCoord;
    }
    
    public void subtract(DoublesDoublet other)
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
        if (other.getClass() == DoublesDoublet.class)
        {
            DoublesDoublet that = (DoublesDoublet) other;
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
