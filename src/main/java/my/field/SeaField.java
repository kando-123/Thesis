package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public class SeaField extends NaturalField
{
    public SeaField()
    {
        super(FieldType.SEA);
    }
    
    @Override
    public boolean isMarine()
    {
        return true;
    }
}
