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
}
