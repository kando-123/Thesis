package my.field;

import java.util.HashMap;
import java.util.Map;
import my.utils.IntegerLinearFunction;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingPriceCalculator
{
    private final Map<FieldType, IntegerLinearFunction> functions;
    
    private BuildingPriceCalculator()
    {
        functions = new HashMap<>();
        for (var value : FieldType.values())
        {
            if (value.isBuilding())
            {
                functions.put(value, new IntegerLinearFunction(value.getPriceIncrement(), value.getInitialPrice()));
            }
        }
    }
    
    private static BuildingPriceCalculator instance = null;
    
    public static BuildingPriceCalculator getInstance()
    {
        if (instance == null)
        {
            instance = new BuildingPriceCalculator();
        }
        return instance;
    }
    
    public int calculatePrice(FieldType type, int index)
    {
        if (type.isBuilding())
        {
            return functions.get(type).computeFor(index);
        }
        else
        {
            return 0;
        }
    }
}
