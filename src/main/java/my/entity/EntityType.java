package my.entity;

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
        String name = name();
        return String.format("/Entities/%s%s.png",
                name.substring(0, 1).toUpperCase(),
                name.substring(1).toLowerCase());
    }
    
    public String getIconFile()
    {
        String name = name();
        return String.format("/iEntities/i%s%s.png",
                name.substring(0, 1).toUpperCase(),
                name.substring(1).toLowerCase());
    }
}
