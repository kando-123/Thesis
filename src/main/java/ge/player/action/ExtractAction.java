package ge.player.action;

import ge.entity.Entity;
import ge.entity.EntityType;
import ge.field.Field;
import ge.main.ExtractAndMoveCommand;
import ge.main.GameplayManager;
import ge.utilities.Hex;
import ge.utilities.Invoker;
import ge.world.WorldAccessor;
import java.util.Random;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ExtractAction extends Action<GameplayManager>
{
    private final Field origin;
    private final WorldAccessor accessor;

    public ExtractAction(Field origin, WorldAccessor accessor)
    {
        this.origin = origin;
        this.accessor = accessor;
    }
    
    private static final Random RANDOM = new Random();
    
    @Override
    public void perform(Invoker<GameplayManager> invoker)
    {
        var extrahend = origin.getEntity();
        
        var probationary = Entity.newInstance(extrahend.getExtractedType(),
                extrahend.getOwner(), extrahend.getNumber() - 1);
        probationary.setMovable(true);
        
        int number = extrahend.getNumber();
        int count = RANDOM.nextInt(Entity.MINIMAL_NUMBER, number);
        
        var range = probationary.range(origin.getHex(), accessor).toArray(Hex[]::new);
        
        if (range.length == 0)
        {
            System.out.println("Extraction error at: " + origin.getHex());
        }
        else
        {
            var hex = range.length == 1 ? range[0] : range[RANDOM.nextInt(range.length)];
            var field = accessor.getField(hex);

            invoker.invoke(new ExtractAndMoveCommand(origin, field, count));
        }
    }

    @Override
    public int weight()
    {
        final int disembarkWeight = 20;
        final int splitTroopWeight = 5;
        return (origin.getEntity().type() == EntityType.NAVY)
                ? disembarkWeight
                : splitTroopWeight;
    }
}
