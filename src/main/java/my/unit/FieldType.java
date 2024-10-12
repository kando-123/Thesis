package my.unit;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum FieldType
{
    BARRACKS,
    CAPITAL,
    FARMFIELD,
    FORTRESS,
    GRASS,
    MEADOW,
    MINE,
    MOUNTAINS,
    SEA,
    SHIPYARD,
    TOWN,
    VILLAGE,
    WOOD;

    @Override
    public String toString()
    {
        String name = name();
        return name.substring(0, 1).concat(name.substring(1).toLowerCase());
    }
    
    public String getFile()
    {
        return String.format("/Fields/%s.png", toString());
    }

    public String getIconFile()
    {
        return String.format("/iFields/i%s.png", toString());
    }

    public static FieldType[] buildings()
    {
        return new FieldType[]
        {
            /* Commercial Fields */
            VILLAGE, FARMFIELD, TOWN, MINE,
            
            /* Spawner Fields */
            BARRACKS, SHIPYARD,
            
            /* Defense Fields */
            FORTRESS
        };
    }
}
