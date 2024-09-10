package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends AbstractField
{
    protected CapitalField()
    {
        super(FieldType.CAPITAL);
    }
    
    @Override
    final public boolean isCapital()
    {
        return true;
    }
}
