/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldParameters
{
    public final int worldSide;
    public final double landThreshold;
    public final double mountainsThreshold;
    public final double woodsThreshold;
    
    public WorldParameters(int side, double land, double mountains, double woods)
    {
        /* To check if the parameters are correct. */
        worldSide = side;
        landThreshold = land;
        mountainsThreshold = mountains;
        woodsThreshold = woods;
    }
}
