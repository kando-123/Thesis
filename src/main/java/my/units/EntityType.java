package my.units;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum EntityType
{
    INFANTRY,
    CAVALRY,
    NAVY;
    
    public String file;
    
    private EntityType()
    {
        String name = name();
        file = String.format("/Entities/%s%s.png",
                name.substring(0, 1).toUpperCase(),
                name.substring(1).toLowerCase());
    }
}
