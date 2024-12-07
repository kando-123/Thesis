package ge.field;

import ge.utilities.*;
import ge.world.*;
import javax.swing.*;

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

    private final int intercept;
    private final int slope;
    
    public final String resource;

    private BuildingType(int intercept, int slope)
    {
        this.intercept = intercept;
        this.slope = slope;
        
        resource = name().substring(0, 1).concat(name().substring(1).toLowerCase());
    }
    
    public int price(int number)
    {
        return intercept + number * slope;
    }
    
    private static final FieldAssetManager ASSET_MANAGER = FieldAssetManager.getInstance();
    
    public Icon icon()
    {
        return ASSET_MANAGER.getIcon(resource);
    }

    @Override
    public String toString()
    {
        return resource;
    }

    public UnaryPredicate<Field> predicate(WorldAccessor accessor)
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield (f) -> f instanceof PlainsField;
            }
            case FARM ->
            {
                yield (f) ->
                {
                    if (f instanceof PlainsField)
                    {
                        for (var h : f.getHex().neighbors())
                        {
                            var n = accessor.getField(h);
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
                yield (f) -> f instanceof ContinentalField;
            }
            case MINE ->
            {
                yield (f) -> f instanceof MountainsField;
            }
            case SHIPYARD ->
            {
                yield (f) ->
                {
                    if (f instanceof PlainsField)
                    {
                        for (var h : f.getHex().neighbors())
                        {
                            var n = accessor.getField(h);
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
                yield (f) -> f instanceof PlainsField;
            }
            case VILLAGE ->
            {
                yield (f) -> f instanceof PlainsField;
            }
        };
    }
    
    public String description()
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
    
    public String conditions()
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
    
    public String pricing()
    {
        return String.format("The first %s costs %d Ħ; every next costs %d Ħ more.",
                resource, intercept, slope);
    }
    
    public boolean isCommercial()
    {
        return switch (this)
        {
            case FARM, MINE, TOWN, VILLAGE ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }
    
    public boolean isSpawner()
    {
        return this == BARRACKS || this == SHIPYARD;
    }
    
    public boolean isFortification()
    {
        return this == FORTRESS;
    }
}
