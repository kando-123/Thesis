package my.field;

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

    @Deprecated
    public boolean isBuilding()
    {
        return isProperty() && !isCapital();
    }

    @Deprecated
    public boolean isCapital()
    {
        return this == CAPITAL;
    }

    @Deprecated
    public boolean isProperty()
    {
        return !isNatural();
    }

    @Deprecated
    public boolean isNatural()
    {
        return switch (this)
        {
            case GRASS, MEADOW, MOUNTAINS, SEA, WOOD ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }

    @Deprecated
    public String getDescription()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield "Barracks are where you can place the new entities you hire.";
            }
            case FARMFIELD ->
            {
                yield "Farmfield brings extra HexCoins after every round.";
            }
            case FORTRESS ->
            {
                yield "Fortress gives additional defence points.";
                /* Maybe a fortress could be transformed into a capital? */
            }
            case MINE ->
            {
                yield "Mine brings extra HexCoins after every round.";
            }
            case SHIPYARD ->
            {
                yield "Shipyard is where you can place the new ships.";
            }
            case TOWN ->
            {
                yield "Town brings extra HexCoins after every round.";
                /* Maybe a town could be transformed into a fortress? */
            }
            case VILLAGE ->
            {
                yield "Village brings extra HexCoin after every round."
                      + " The income is greater when the village has farmfields.";
            }
            default ->
            {
                yield "(Capital or a natural field.)";
            }
        };
    }

    @Deprecated
    public String getConditions()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield "You need a land field to build barracks.";
            }
            case FARMFIELD ->
            {
                yield "You need a land field adjacent to a village to build a farmfield.";
            }
            case FORTRESS ->
            {
                yield "You need a land field or a mountain field to build a fortress.";
            }
            case MINE ->
            {
                yield "You need a mountain field to build a mine.";
            }
            case SHIPYARD ->
            {
                yield "You need a land field that is adjacent to a see field to build a shipyard.";
            }
            case TOWN ->
            {
                yield "You need a land field to build a town.";
            }
            case VILLAGE ->
            {
                yield "You need a land field to build a village.";
            }
            default ->
            {
                yield "(Not purchasable.)";
            }
        };
    }
    
    @Deprecated
    public int getInitialPrice()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield 250;
            }
            case FARMFIELD ->
            {
                yield 100;
            }
            case FORTRESS ->
            {
                yield 400;
            }
            case MINE ->
            {
                yield 200;
            }
            case SHIPYARD ->
            {
                yield 600;
            }
            case TOWN ->
            {
                yield 300;
            }
            case VILLAGE ->
            {
                yield 200;
            }
            default ->
            {
                yield 0;
            }
        };
    }
    
    @Deprecated
    public int getPriceIncrement()
    {
        return switch (this)
        {
            case BARRACKS ->
            {
                yield 50;
            }
            case FARMFIELD ->
            {
                yield 10;
            }
            case FORTRESS ->
            {
                yield 100;
            }
            case MINE ->
            {
                yield 50;
            }
            case SHIPYARD ->
            {
                yield 75;
            }
            case TOWN ->
            {
                yield 50;
            }
            case VILLAGE ->
            {
                yield 25;
            }
            default ->
            {
                yield 0;
            }
        };
    }
    
    @Deprecated
    private static int countPurchasables()
    {
        int count = 0;
        for (var value : FieldType.values())
        {
            if (value.isBuilding())
            {
                ++count;
            }
        }
        return count;
    }
    
    @Deprecated
    public static final int BUILDINGS_COUNT = countPurchasables();
}