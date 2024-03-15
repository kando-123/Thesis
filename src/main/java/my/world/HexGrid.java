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
public class HexGrid
{
    private Map<Integer, Map<Integer, Field>> fields;
    
    public HexGrid()
    {
        fields = new HashMap<>();
    }
    
    public void put(Hex coords, Field field)
    {
        int p = coords.getP();
        int q = coords.getQ();
        if (!fields.containsKey(p))
        {
            fields.put(p, new HashMap<>());
        }
        fields.get(p).put(q, field);
    }
    
    public Field get(Hex coords)
    {
        int p = coords.getP();
        int q = coords.getQ();
        Map<Integer, Field> pMap = fields.get(p);
        return (pMap != null) ? pMap.get(q) : null;
    }
}
