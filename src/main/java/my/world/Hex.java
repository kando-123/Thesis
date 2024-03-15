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
    private final int pCoord;
    private final int qCoord;
    private final int rCoord;
    
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
        int p = factor * pCoord;
        int q = factor * qCoord;
        int r = factor * rCoord;
        return new Hex(p, q, r);
    }
    
    public int distance(Hex other)
    {
        int pDistance = Math.abs(pCoord - other.pCoord);
        int qDistance = Math.abs(qCoord - other.qCoord);
        int rDistance = Math.abs(rCoord - other.rCoord);
        return (pDistance + qDistance + rDistance) / 2;
    }
    
    public Hex neighbor(Direction direction)
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
        List<Hex> hexes = new ArrayList<>(Direction.values().length);
        for (var direction : Direction.values())
        {
            hexes.add(neighbor(direction));
        }
        return hexes;
    }
    
    @Override
    public String toString()
    {
        return String.format("Hex(p=%i, q=%i, r=%i)", pCoord, qCoord, rCoord);
    }
}
