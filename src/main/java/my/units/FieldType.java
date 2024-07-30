package my.units;

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

    private FieldType()
    {
        String filename = name().substring(0, 1).concat(name().substring(1).toLowerCase());
        path = String.format("/Fields/%s.png", filename);
    }

    public boolean isPurchasable()
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
            case TOWN, VILLAGE, BARRACKS ->
            {
                yield "A land field is needed.";
            }
            case FARMFIELD ->
            {
                yield "A land field adjacent to a village is needed.";
            }
            case MINE ->
            {
                yield "A mountain field is needed.";
            }
            case SHIPYARD ->
            {
                yield "A land field adjacent to a see field is needed.";
            }
            case FORTRESS ->
            {
                yield "A land field or a mountains field is needed.";
            }
            default ->
            {
                yield "(Not purchasable.)";
            }
        };
    }
    
    private static int countPurchasables()
    {
        int count = 0;
        for (var value : FieldType.values())
        {
            if (value.isPurchasable())
            {
                ++count;
            }
        }
        return count;
    }
    
    public static final int PURCHASABLES_COUNT = countPurchasables();
}