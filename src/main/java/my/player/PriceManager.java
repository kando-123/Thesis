package my.player;

import java.util.HashMap;
import java.util.Map;
import my.units.FieldType;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PriceManager
{
    private final Map<FieldType, Integer> counters;
    
    private static class LinearIntFunction
    {
        private int slope, intercept;
        
        public LinearIntFunction(int slope, int intercept)
        {
            this.slope = slope;
            this.intercept = intercept;
        }
        
        public int computeFor(int x)
        {
            return slope * x + intercept;
        }
    }
    
    private final Map<FieldType, LinearIntFunction> functions;
    
    public PriceManager()
    {
        counters = new HashMap<>(FieldType.PURCHASABLES_COUNT);
        functions = new HashMap<>(FieldType.PURCHASABLES_COUNT);
        for (var value : FieldType.values())
        {
            if (value.isPurchasable())
            {
                counters.put(value, 0);
                functions.put(value, new LinearIntFunction(value.getInitialPrice(), value.getPriceIncrement()));
            }
        }
    }
    
    public void registerPurchase(FieldType purchasable)
    {
        Integer count = counters.get(purchasable);
        if (count != null)
        {
            counters.put(purchasable, count + 1);
        }
    }
    
    public int getPriceForNext(FieldType field)
    {
        if (field.isPurchasable())
        {
            return functions.get(field).computeFor(counters.get(field));
        }
        else
        {
            return 0;
        }
    }
    
}
