package my.field;

import java.util.Arrays;

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

    public String getFile()
    {
        String filename = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        return String.format("/Fields/%s.png", filename);
    }

    public String getIconFile()
    {
        String filename = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        return String.format("/iFields/i%s.png", filename);
    }

    public static FieldType[] buildings()
    {
        return new FieldType[]
        {
            BARRACKS, FARMFIELD, FORTRESS, MINE, SHIPYARD, TOWN, VILLAGE
        };
    }
}
