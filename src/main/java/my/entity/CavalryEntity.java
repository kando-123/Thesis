package my.entity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CavalryEntity extends AbstractEntity
{
    public CavalryEntity()
    {
        super(EntityType.CAVALRY);
        
        priceIntercept = 0;
        priceSlope = 25;
    }

    @Override
    public String getDescription()
    {
        return "Cavalry is a quick land entity. It passes plains faster than Infantry. "
                + "It is, however, slower in mountains. It cannot embark ships.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Cavalry, you need Barracks or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Cavalry costs %d Ħ × number of soldiers.",
                priceSlope);
    }
}
