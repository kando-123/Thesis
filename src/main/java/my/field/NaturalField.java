package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class NaturalField extends AbstractField
{
    protected NaturalField(FieldType type)
    {
        super(type);
    }
    
    @Override
    final public boolean isNatural()
    {
        return true;
    }
}
