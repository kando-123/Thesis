package ge.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldConfig
{
    final int worldSide;
    final double seaPercentage;
    final double mountainsPercentage;

    public WorldConfig(int worldSide, double seaPercentage, double mountainsPercentage)
    {
        this.worldSide = worldSide;
        this.seaPercentage = seaPercentage;
        this.mountainsPercentage = mountainsPercentage;
    }
}
