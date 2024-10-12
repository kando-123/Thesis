package my.unit;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum EntityType
{
    INFANTRY(3, 2, 0),
    CAVALRY(4, 1, 0),
    NAVY(0, 0, 5);
    
    public final int plainsSpeed;
    public final int mountainsSpeed;
    public final int seaSpeed;
    
    private EntityType(int plainsSpeed, int mountainsSpeed, int seaSpeed)
    {
        this.plainsSpeed = plainsSpeed;
        this.mountainsSpeed = mountainsSpeed;
        this.seaSpeed = seaSpeed;
    }

    public String getFile()
    {
        return String.format("/Entities/%s.png", toString());
    }
    
    public String getIconFile()
    {
        return String.format("/iEntities/i%s.png", toString());
    }
    
    @Override
    public String toString()
    {
        String name = name();
        return name.substring(0, 1).concat(name.substring(1).toLowerCase());
    }
}
