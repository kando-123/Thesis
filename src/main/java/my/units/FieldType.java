package my.units;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum FieldType
{
    BARRACKS("/Fields/Barracks.png"),
    CAPITAL("/Fields/Capital.png"),
    FARMFIELD("/Fields/Farmfield.png"),
    FORTRESS("/Fields/Fortress.png"),
    LAND("/Fields/Land1.png", "/Fields/Land2.png"),
    MINE("/Fields/Mine.png"),
    MOUNTS("/Fields/Mounts.png"),
    SEA("/Fields/Sea1.png", "/Fields/Sea2.png"),
    SHIPYARD("/Fields/Shipyard.png"),
    TOWN("/Fields/Town.png"),
    VILLAGE("/Fields/Village.png"),
    WOOD("/Fields/Wood.png");
    
    public final List<String> paths;
    
    private FieldType(String... path)
    {
        this.paths = Collections.unmodifiableList(Arrays.asList(path));
    }
}
