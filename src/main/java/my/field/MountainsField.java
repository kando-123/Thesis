package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MountainsField extends ContinentalField
{
    protected MountainsField()
    {
        super(FieldType.MOUNTAINS);
    }
    
    @Override
    final public boolean isMountainous()
    {
        return true;
    }
}
