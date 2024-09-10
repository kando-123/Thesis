package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class ContinentalField extends NaturalField
{
    protected ContinentalField(FieldType type)
    {
        super(type);
    }
    
    @Override
    final public boolean isContinental()
    {
        return true;
    }
}
