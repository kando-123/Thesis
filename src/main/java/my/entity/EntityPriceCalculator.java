package my.entity;

import java.util.HashMap;
import java.util.Map;
import my.utils.IntegerLinearFunction;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityPriceCalculator
{
    Map<EntityType, IntegerLinearFunction> functions;

    private EntityPriceCalculator()
    {
        functions = new HashMap<>(3);
        functions.put(EntityType.INFANTRY,
                new IntegerLinearFunction(0, EntityType.INFANTRY.getPrice()));
        functions.put(EntityType.CAVALRY,
                new IntegerLinearFunction(0, EntityType.CAVALRY.getPrice()));
        functions.put(EntityType.NAVY,
                new IntegerLinearFunction(EntityType.INFANTRY.getPrice(), EntityType.NAVY.getPrice()));
    }

    private static EntityPriceCalculator instance = null;

    public static EntityPriceCalculator getInstance()
    {
        if (instance == null)
        {
            instance = new EntityPriceCalculator();
        }
        return instance;
    }

    public int getPriceFor(int number, EntityType type)
    {
        assert (type != null);
        return functions.get(type).computeFor(number);
    }
}
