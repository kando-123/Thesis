package my.player;

import java.util.HashSet;
import java.util.Set;
import my.field.AbstractField;
import my.field.FieldType;
import my.utils.Hex;
import my.world.World;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Country
{
    private final Player owner;
    
    private final World.Accessor accessor;
    private final Set<Hex> territory;
    private Hex capital;
    
    public Country(Player owner, World.Accessor worldAccessor)
    {
        this.owner = owner;
        this.accessor = worldAccessor;
        
        territory = new HashSet<>();
    }
    
    public Hex setCapital(Hex newCapital)
    {
        Hex oldCapital = capital;
        capital = newCapital;
        return oldCapital;
    }
    
    public Hex getCapitalHex()
    {
        return capital;
    }
    
    public void addField(AbstractField field)
    {
        field.setOwner(owner);
        territory.add(field.getHex());
    }
    
    public void removeField(AbstractField field)
    {
        field.setOwner(null);
        territory.remove(field.getHex());
    }
    
    public int count(FieldType type)
    {
        return (int) territory.stream()
                .filter((hex) -> accessor.getFieldAt(hex).getType() == type)
                .count();
    }
    
    public interface UnaryPredicate<T>
    {
        public boolean test(T item);
    }
    
    public int count(UnaryPredicate<AbstractField> predicate)
    {
        return (int) territory.stream()
                .filter((hex) -> predicate.test(accessor.getFieldAt(hex)))
                .count();
    }
    
    public Set<Hex> getTerritory()
    {
        return territory;
    }
}
