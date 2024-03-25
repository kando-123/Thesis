/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.util.Objects;

/**
 *
 * @author Kay Jay O'Nail
 * @param <N> 
 */
public class Pixel
{
    private final int xCoord;
    private final int yCoord;
    
    public Pixel(int x, int y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }
    
    public int getX()
    {
        return xCoord;
    }
    
    public int getY()
    {
        return yCoord;
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
