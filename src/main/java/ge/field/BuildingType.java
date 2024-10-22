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
    public final String resource;

    private BuildingType(int intercept, int slope)
    {
        priceIntercept = intercept;
        priceSlope = slope;
        resource = name().substring(0, 1).concat(name().substring(1).toLowerCase());
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
    
    public String getDescription()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield "Barracks is where you can place the new Infantry and Cavalry you hire.";
            }
            case FARM ->
            {
                yield "Farmfield brings extra %d Ħ after every round.".formatted(FarmField.INCOME);
            }
            case FORTRESS ->
            {
                yield "Fortress gives additional, field-fixed defence points.";
            }
            case MINE ->
            {
                yield "Mine brings extra %d Ħ after every round.".formatted(MineField.INCOME);
            }
            case SHIPYARD ->
            {
                yield "Shipyard is where you can place the new Navy ships.";
            }
            case TOWN ->
            {
                yield "Town brings extra %d Ħ after every round.".formatted(TownField.INCOME);
            }
            case VILLAGE ->
            {
                yield "Village brings extra %d Ħ after every round.".formatted(VillageField.INCOME);
            }
        };
    }
    
    public String getConditions()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield "To build barracks, you need a plains field.";
            }
            case FARM ->
            {
                yield "To build a farmfield, you need a plains field adjacent to a village.";
            }
            case FORTRESS ->
            {
                yield "To build a fortress, you need a continental field.";
            }
            case MINE ->
            {
                yield "To build a mine, you need a mountain field.";
            }
            case SHIPYARD ->
            {
                yield "To build a shipyard, you need a plains field adjacent to a see field.";
            }
            case TOWN ->
            {
                yield "To build a town, you need a plains field.";
            }
            case VILLAGE ->
            {
                yield "To build a village, you need a plains field.";
            }
        };
    }
    
    public String getPricing()
    {
        return String.format("The first %s costs %d Ħ; every next costs %d Ħ more.",
                resource, priceIntercept, priceSlope);
    }
}
