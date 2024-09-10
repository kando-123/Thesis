package my.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class CommercialField extends BuildingField
{
    protected CommercialField(FieldType type)
    {
        super(type);
    }
    
    @Override
    public boolean isCommercial()
    {
        return true;
    }
}
