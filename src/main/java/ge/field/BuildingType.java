package ge.field;

import ge.utilities.*;
import ge.world.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum BuildingType
{
    BARRACKS(250, 25),
    FARM(100, 10),
    FORTRESS(750, 50),
    MINE(250, 25),
    SHIPYARD(700, 100),
    TOWN(500, 50),
    VILLAGE(150, 15);

    public final int priceIntercept;
    public final int priceSlope;
    public final String resourceName;

    private BuildingType(int intercept, int slope)
    {
        priceIntercept = intercept;
        priceSlope = slope;
        resourceName = name().substring(0, 1).concat(name().substring(1).toLowerCase());
    }
    
    public int price(int number)
    {
        return priceIntercept + number * priceSlope;
    }

    public BinaryPredicate<Field, WorldAccessor> getPredicate()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield (f, a) -> f instanceof PlainsField;
            }
            case FARM ->
            {
                yield (f, a) ->
                {
                    if (f instanceof PlainsField)
                    {
                        for (var h : f.getHex().neighbors())
                        {
                            var n = a.getField(h);
                            if (n != null && n instanceof VillageField)
                            {
                                return true;
                            }
                        }
                    }
                    return false;
                };
            }
            case FORTRESS ->
            {
                yield (f, a) -> f instanceof ContinentalField;
            }
            case MINE ->
            {
                yield (f, a) -> f instanceof MountainsField;
            }
            case SHIPYARD ->
            {
                yield (f, a) ->
                {
                    if (f instanceof PlainsField)
                    {
                        for (var h : f.getHex().neighbors())
                        {
                            var n = a.getField(h);
                            if (n != null && n instanceof SeaField)
                            {
                                return true;
                            }
                        }
                    }
                    return false;
                };
            }
            case TOWN ->
            {
                yield (f, a) -> f instanceof PlainsField;
            }
            case VILLAGE ->
            {
                yield (f, a) -> f instanceof PlainsField;
            }
        };
    }
}
