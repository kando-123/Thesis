package my.player;

import java.util.HashMap;
import java.util.Map;
import my.units.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PriceCalculator
{
    private static class IntegerLinearFunction
    {
        private final int slope;
        
        private final int intercept;
        
        public IntegerLinearFunction(int slope, int intercept)
        {
            this.slope = slope;
            this.intercept = intercept;
        }
        
        public int computeFor(int x)
        {
            return slope * x + intercept;
        }
    }
    
    private final Map<FieldType, IntegerLinearFunction> functions;
    
    public PriceCalculator()
    {
        functions = new HashMap<>(FieldType.BUILDINGS_COUNT);
        for (var value : FieldType.values())
        {
            if (value.isBuilding())
            {
                functions.put(value, new IntegerLinearFunction(value.getInitialPrice(), value.getPriceIncrement()));
            }
        }
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
