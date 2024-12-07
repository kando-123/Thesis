package ge.entity;

import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum EntityType
{
    CAVALRY(0, 25),
    INFANTRY(0, 20),
    NAVY(130, INFANTRY.priceSlope);

    private final int priceIntercept;
    private final int priceSlope;
    public final String resource;
    
    private static final EntityAssetManager ASSET_MANAGER = EntityAssetManager.getInstance();

    private EntityType(int intercept, int slope)
    {
        priceIntercept = intercept;
        priceSlope = slope;
        resource = name().substring(0, 1).concat(name().substring(1).toLowerCase());
    }

    public int price(int number)
    {
        return priceIntercept + number * priceSlope;
    }
    
    public int maxNumber(int money)
    {
        int number = (money - priceIntercept) / priceSlope;
        return (number < 0) ? 0 : number;
    }
    
    @Override
    public String toString()
    {
        return resource;
    }
    
    public Icon icon()
    {
        return ASSET_MANAGER.getIcon(resource);
    }

    public String description()
    {
        return switch (this)
        {
            case CAVALRY ->
            {
                yield "Cavalry is a quick land entity. It passes plains faster than Infantry. "
                      + "It is, however, slower in mountains. It cannot embark ships.";
            }
            case INFANTRY ->
            {
                yield "Infantry is a slow land entity. It can, however, travel aboard ships. "
                      + "It also passes mountains quicker than Cavalry.";
            }
            case NAVY ->
            {
                yield "Navy is the marine entity. It can transport Infantry onboard.";
            }
        };
    }

    public String conditions()
    {
        return switch (this)
        {
            case CAVALRY ->
            {
                yield "To spawn Infantry, you need Barracks or Capital. (No entity must be there.)";
            }
            case INFANTRY ->
            {
                yield "To spawn Cavalry, you need Barracks or Capital. (No entity must be there.)";
            }
            case NAVY ->
            {
                yield "To spawn Navy, you need Shipyard. (No entity must be there.)";
            }
        };
    }

    public String pricing()
    {
        return switch (this)
        {
            case CAVALRY ->
            {
                yield "A troop of Infantry costs %d Ħ × number of soldiers.".formatted(priceSlope);
            }
            case INFANTRY ->
            {
                yield "A troop of Cavalry costs %d Ħ × number of soldiers.".formatted(priceSlope);
            }
            case NAVY ->
            {
                yield "A troop of Navy costs %d Ħ for the ship + %d Ħ × number of soldiers."
                    .formatted(priceIntercept, priceSlope);
            }
        };
    }
}
