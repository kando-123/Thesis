package my.entity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InfantryEntity extends AbstractEntity
{
    public InfantryEntity()
    {
        super(EntityType.INFANTRY);
        
        priceIntercept = 0;
        priceSlope = 20;
    }

    @Override
    public String getDescription()
    {
        return "Infantry is a slow land entity. It can, however, travel aboard ships. "
                + "It also passes mountains quicker than Cavalry.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Infantry, you need Barracks or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Infantry costs %d Ħ × number of soldiers.",
                priceSlope);
    }
}
