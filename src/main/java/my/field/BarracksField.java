package my.field;

import my.entity.AbstractEntity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BarracksField extends SpawnerField
{
    public BarracksField()
    {
        super(FieldType.BARRACKS);
        
        priceIntercept = 250;
        priceSlope = 50;
    }

    @Override
    public String getDescription()
    {
        return "Barracks is where you can place the new Infantry and Cavalry you hire.";
    }

    @Override
    public String getCondition()
    {
        return "To build barracks, you need a plains field.";
    }
    
    @Override
    public boolean canSpawn(AbstractEntity entity)
    {
        return isFree() && switch (entity.getType())
        {
            case INFANTRY, CAVALRY ->
            {
                yield true;
            }
            default ->
            {
                yield false;
            }
        };
    }
}
