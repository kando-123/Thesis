package my.player;

import java.util.HashSet;
import java.util.Set;
import my.field.AbstractField;
import my.field.FieldType;
import my.utils.Hex;
import my.world.WorldAccessor;
import my.world.WorldMarker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Country
{
    private final Player owner;

    private final WorldAccessor accessor;
    private final Set<Hex> territory;
    private Hex capital;

    public Country(Player owner, WorldAccessor worldAccessor)
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
        territory.add(field.getHex());
    }

    public void removeField(AbstractField field)
    {
        territory.remove(field.getHex());
    }

    public int count(FieldType type)
    {
        return (int) territory.stream()
                .filter((hex) -> accessor.getFieldAt(hex).getType() == type)
                .count();
    }

    public int count(UnaryPredicate<AbstractField> predicate)
    {
        return (int) territory.stream()
                .filter((hex) -> predicate.test(accessor.getFieldAt(hex)))
                .count();
    }

    public void markIf(UnaryPredicate<Hex> predicate, WorldMarker marker)
    {
        for (var hex : territory)
        {
            if (predicate.test(hex))
            {
                marker.mark(hex);
            }
        }
    }
    
    public boolean isAny(UnaryPredicate<Hex> predicate)
    {
        for (var hex : territory)
        {
            if (predicate.test(hex))
            {
                return true;
            }
        }
        return false;
    }
}
