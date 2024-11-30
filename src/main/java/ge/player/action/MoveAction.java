package ge.player.action;

import ge.field.Field;
import ge.main.GameplayManager;
import ge.main.MoveCommand;
import ge.utilities.Invoker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class MoveAction extends Action<GameplayManager>
{
    private final Field origin;
    private final Field target;

    public MoveAction(Field origin, Field target)
    {
        this.origin = origin;
        this.target = target;
    }
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        invoker.invoke(new MoveCommand(origin, target));
    }
}
