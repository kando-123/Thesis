package my.field;

import my.field.FieldType;

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
    
    public abstract int getIncome();
}
