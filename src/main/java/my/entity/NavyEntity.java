package my.entity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class NavyEntity extends AbstractEntity
{
    private int sailors;

    public NavyEntity()
    {
        super(EntityType.NAVY);
        
        priceIntercept = 130;
        priceSlope = 20;
    }

    @Override
    public String getDescription()
    {
        return "Navy is the marine entity. It can transport Infantry onboard.";
    }

    @Override
    public String getCondition()
    {
        return "To spawn Navy, you need Shipyard or Capital. (No entity must be there.)";
    }
    
    @Override
    public String getPricing()
    {
        return String.format("A troop of Navy costs %d Ħ for the ship + %d Ħ × number of soldiers.",
                priceIntercept, priceSlope);
    }
}
