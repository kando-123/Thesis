package my.field;

import my.field.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class FortificationField extends BuildingField implements Fortification
{
    protected FortificationField(FieldType type)
    {
        super(type);
    }
}
