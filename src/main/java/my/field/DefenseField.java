package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class DefenseField extends BuildingField implements Defense
{
    protected DefenseField(FieldType type)
    {
        super(type);
    }
    
    @Override
    final public boolean isDefense()
    {
        return true;
    }
}
