package my.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import my.units.Field;
import my.utils.DoublesDoublet;
import my.utils.Hex;
import my.world.World;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Country
{
    private final Map<Hex, Field> territory;
    private Field capital;
    
    public Country()
    {
        territory = new HashMap<>();
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
        territory.put(field.getHex(), field);
    }
    
    public void removeField(Hex hex)
    {
        territory.get(hex).setOwner(null);
        territory.remove(hex);
    }
    
    public Set<Hex> getFieldHexes()
    {
        return territory.keySet();
    }
}
