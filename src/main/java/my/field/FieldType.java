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

    public final String path;
    public final String iPath;

    private FieldType()
    {
        String filename = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        path = String.format("/Fields/%s.png", filename);
        iPath = String.format("/iFields/i%s.png", filename);
    }

    public boolean isBuilding()
    {
        return isProperty() && !isCapital();
    }

    public boolean isCapital()
    {
        return this == CAPITAL;
    }

    public boolean isProperty()
    {
        return !isNatural();
    }

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

    public boolean isMarine()
    {
        return this == SEA;
    }

    public boolean isContinental()
    {
        return switch (this)
        {
            case GRASS, MEADOW, MOUNTAINS, WOOD ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }

    public boolean isPlains()
    {
        return switch (this)
        {
            case GRASS, MEADOW, WOOD ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }

    public boolean isMountainous()
    {
        return this == MOUNTAINS;
    }

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
                yield "Farmfield gives you extra HexCoins after every round.";
            }
            case FORTRESS ->
            {
                yield "Fortress NEEDS SPECIFYING AFTER DEFINING MILITATION MECHANICS .";
                /* Maybe a fortress could be transformed into a capital? */
            }
            case MINE ->
            {
                yield "Mine gives you extra HexCoins after every round.";
            }
            case SHIPYARD ->
            {
                yield "Shipyard is where you can place the new ships.";
            }
            case TOWN ->
            {
                yield "Town gives you extra HexCoins after every round.";
                /* Maybe a town could be transformed into a fortress? */
            }
            case VILLAGE ->
            {
                yield "Village gives you extra HexCoin after every round."
                      + " The income of a village is greater when it has farmfields adjacent to it.";
            }
            default ->
            {
                yield "(Capital or a natural field.)";
            }
        };
    }

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
    
    public static final int BUILDINGS_COUNT = countPurchasables();
}