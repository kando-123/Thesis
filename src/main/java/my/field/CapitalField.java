package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class CapitalField extends AbstractField implements Defense, Spawner
{
    public CapitalField()
    {
        super(FieldType.CAPITAL);
    }
    
    @Override
    final public boolean isCapital()
    {
        return true;
    }
    
    @Override
    final public boolean isDefense()
    {
        return true;
    }
    
    @Override
    final public boolean isSpawner()
    {
        return true;
    }
}