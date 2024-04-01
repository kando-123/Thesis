/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.util.*;

/**
 * 
 * @author Kay Jay O'Nail
 */
public class Hex
{
    private int pCoord;
    private int qCoord;
    private int rCoord;
    
    private Hex(int p, int q, int r)
    {
        assert (p + q + r == 0);
        
        pCoord = p;
        qCoord = q;
        rCoord = r;
    }
    
    public static Hex make(int p, int q, int r)
    {
        return (p + q + r == 0) ? new Hex(p, q, r) : null;
    }
    
    public Hex clone()
    {
        return new Hex(pCoord, qCoord, rCoord);
    }
    
    public int getP()
    {
        return pCoord;
    }
    
    public int getQ()
    {
        return qCoord;
    }
    
    public int getR()
    {
        return rCoord;
    }
    
    public Hex plus(Hex other)
    {
        int p = pCoord + other.pCoord;
        int q = qCoord + other.qCoord;
        int r = rCoord + other.rCoord;
        return new Hex(p, q, r);
    }
    
    public Hex minus(Hex other)
    {
        int p = pCoord - other.pCoord;
        int q = qCoord - other.qCoord;
        int r = rCoord - other.rCoord;
        return new Hex(p, q, r);
    }
    
    public Hex times(int factor)
    {
        int p = pCoord * factor;
        int q = qCoord * factor;
        int r = rCoord * factor;
        return new Hex(p, q, r);
    }
    
    public void add(Hex other)
    {
        pCoord += other.pCoord;
        qCoord += other.qCoord;
        rCoord += other.rCoord;
    }
    
    public void subtract(Hex other)
    {
        pCoord -= other.pCoord;
        qCoord -= other.qCoord;
        rCoord -= other.rCoord;
    }
    
    public void multiply(int factor)
    {
        pCoord *= factor;
        qCoord *= factor;
        rCoord *= factor;
    }
    
    public int distance(Hex other)
    {
        int pDistance = Math.abs(pCoord - other.pCoord);
        int qDistance = Math.abs(qCoord - other.qCoord);
        int rDistance = Math.abs(rCoord - other.rCoord);
        return (pDistance + qDistance + rDistance) / 2;
    }
    
    public Hex neighbor(HexagonalDirection direction)
    {
        int p = pCoord;
        int q = qCoord;
        int r = rCoord;
        switch (direction)
        {
            case UP ->
            {
                --q;
                ++r;
            }
            case RIGHT_UP ->
            {
                ++p;
                --q;
            }
            case RIGHT_DOWN ->
            {
                ++p;
                --r;
            }
            case DOWN ->
            {
                ++q;
                --r;
            }
            case LEFT_DOWN ->
            {
                --p;
                ++q;
            }
            case LEFT_UP ->
            {
                --p;
                ++r;
            }
        }
        return new Hex(p, q, r);
    }
    
    public List<Hex> neighbors()
    {
        List<Hex> hexes = new ArrayList<>(HexagonalDirection.values().length);
        for (var direction : HexagonalDirection.values())
        {
            hexes.add(neighbor(direction));
        }
        return hexes;
    }
    
    public void shift(HexagonalDirection direction)
    {
        switch (direction)
        {
            case UP ->
            {
                --qCoord;
                ++rCoord;
            }
            case RIGHT_UP ->
            {
                ++pCoord;
                --qCoord;
            }
            case RIGHT_DOWN ->
            {
                ++pCoord;
                --rCoord;
            }
            case DOWN ->
            {
                ++qCoord;
                --rCoord;
            }
            case LEFT_DOWN ->
            {
                --pCoord;
                ++qCoord;
            }
            case LEFT_UP ->
            {
                --pCoord;
                ++rCoord;
            }
        }
    }
    
    public Pixel getCentralPixel(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2;
        int y = (qCoord - rCoord) * innerRadius;
        return new Pixel(x, y);
    }
    
    public Pixel getCornerPixel(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2 - outerRadius;
        int y = (qCoord - rCoord) * innerRadius - innerRadius;
        return new Pixel(x, y);
    }
    
    public Point getCentralPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5;
        double y = (double) (qCoord - rCoord) * innerRadius;
        return new Point(x, y);
    }
    
    public Point getCornerPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5 - outerRadius;
        double y = (double) (qCoord - rCoord) * innerRadius - innerRadius;
        return new Point(x, y);
    }
    
    public static Pixel getCentralPixelOf(int p, int q, int r, int outerRadius, int innerRadius)
    {
        int x = p * outerRadius * 3 / 2;
        int y = (q - r) * innerRadius;
        return new Pixel(x, y);
    }
    
    public static Pixel getCornerPixelOf(int p, int q, int r, int outerRadius, int innerRadius)
    {
        int x = p * outerRadius * 3 / 2 - outerRadius;
        int y = (q - r) * innerRadius - innerRadius;
        return new Pixel(x, y);
    }
    
    public static Point getCentralPointOf(int p, int q, int r, double outerRadius, double innerRadius)
    {
        double x = (double) (p) * outerRadius * 1.5;
        double y = (double) (q - r) * innerRadius;
        return new Point(x, y);
    }
    
    public static Point getCornerPointOf(int p, int q, int r, double outerRadius, double innerRadius)
    {
        double x = (double) (p) * outerRadius * 1.5 - outerRadius;
        double y = (double) (q - r) * innerRadius - innerRadius;
        return new Point(x, y);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other.getClass() == Hex.class)
        {
            Hex hex = (Hex) other;
            return pCoord == hex.pCoord && qCoord == hex.qCoord && rCoord == hex.rCoord;
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
        hash = 19 * hash + this.pCoord;
        hash = 19 * hash + this.qCoord;
        hash = 19 * hash + this.rCoord;
        return hash;
    }
    
    @Override
    public String toString()
    {
        return String.format("Hex@(p=%d, q=%d, r=%d)", pCoord, qCoord, rCoord);
    }
}
