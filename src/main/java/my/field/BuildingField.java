package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingField extends AbstractField
{
    protected BuildingField(FieldType type)
    {
        super(type);
    }
    
    @Override
    public boolean isBuilding()
    {
        return true;
    }
}
