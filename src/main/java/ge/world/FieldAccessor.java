package ge.world;

import ge.field.Field;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public interface FieldAccessor
{
    public Field getField(Hex hex);
}
