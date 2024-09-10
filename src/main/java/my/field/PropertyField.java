package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class PropertyField extends AbstractField
{
    protected PropertyField(FieldType type)
    {
        super(type);
    }
    
    @Override
    public boolean isProperty()
    {
        return true;
    }
}
