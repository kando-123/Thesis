package my.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import my.units.Field;
import my.units.FieldType;
import my.utils.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Country
{
    private final Player owner;
    
    private final Map<Hex, Field> territory;
    private final Map<FieldType, Integer> counters;
    private Field capital;
    
    public Country(Player owner)
    {
        this.owner = owner;
        
        territory = new HashMap<>();
        counters = new HashMap<>();
        for (var value : FieldType.values())
        {
            counters.put(value, 0);
        }
    }
    
    public void setCapital(Field newCapital)
    {
        capital = newCapital;
    }
    
    public Hex getCapitalHex()
    {
        return capital.getHex();
    }
    
    public void addField(Field field)
    {
        field.setOwner(owner);
        territory.put(field.getHex(), field);
        
        int count = counters.get(field.getType());
        counters.put(field.getType(), count + 1);
    }
    
    public void removeField(Field field)
    {
        field.setOwner(null);
        territory.remove(field.getHex());
        
        int count = counters.get(field.getType());
        counters.put(field.getType(), count - 1);
    }
    
    public int getCount(FieldType type)
    {
        return counters.get(type);
    }
    
    public Set<Hex> getFieldHexes()
    {
        return territory.keySet();
    }
}
