package ge.field;

import ge.entity.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class SeaField extends NaturalField
{
    public SeaField(Hex coords)
    {
        super(coords);
    }

    @Override
    public Entity takeEntity()
    {
        if (entity != null && entity instanceof NavyEntity)
        {
            owner = null;
        }
        return super.takeEntity();
    }
}
