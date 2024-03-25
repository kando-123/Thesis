/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Point
{
    private final double xCoord;
    private final double yCoord;
    
    public Point(double x, double y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public double getX()
    {
        return xCoord;
    }
    
    public double getY()
    {
        return yCoord;
    }
    
    public Point plus(Point other)
    {
        double x = xCoord + other.xCoord;
        double y = yCoord + other.yCoord;
        return new Point(x, y);
    }
    
    public Point minus(Point other)
    {
        double x = xCoord - other.xCoord;
        double y = yCoord - other.yCoord;
        return new Point(x, y);
    }
    
    public Point times(int factor)
    {
        double x = factor * xCoord;
        double y = factor * yCoord;
        return new Point(x, y);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other.getClass() == Point.class)
        {
            Point that = (Point) other;
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
