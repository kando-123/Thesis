package ge.player.action;

import ge.field.Field;
import ge.main.GameplayManager;
import ge.utilities.Invoker;
import java.util.Collection;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractAction extends Action<GameplayManager>
{
    private final Field origin;
    private final int maximum;
    private final Collection<Field> range;

    public ExtractAction(Field origin, int maximum, Collection<Field> range)
    {
        this.origin = origin;
        this.maximum = maximum;
        this.range = range;
    }
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        throw new UnsupportedOperationException();
        //invoker.invoke(new MoveCommand(origin, target));
    }

    @Override
    public int weight()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
