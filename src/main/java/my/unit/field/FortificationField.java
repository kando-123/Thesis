package my.unit.field;

import my.unit.FieldType;

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
