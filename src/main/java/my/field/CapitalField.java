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
    public boolean isCapital()
    {
        return true;
    }
}
