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
                yield "Land field";
            }
            case FARMFIELD ->
            {
                yield "Land field adjacent to a village";
            }
            case MINE ->
            {
                yield "Mountain field";
            }
            case SHIPYARD ->
            {
                yield "Land field adjacent to a see field";
            }
            case FORTRESS ->
            {
                yield "Land field or mountains field.";
            }
            default ->
            {
                yield "(Not purchasable.)";
            }
        };
    }
}
