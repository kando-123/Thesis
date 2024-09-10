package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class PlainsField extends ContinentalField
{
    protected PlainsField(FieldType type)
    {
        super(type);
    }
    
    @Override
    public boolean isPlains()
    {
        return true;
    }
}
