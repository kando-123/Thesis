package ge.gui;

import ge.entity.EntityType;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BeginHiringCommand extends Command<GUIManager>
{
    private final EntityType entity;

    public BeginHiringCommand(EntityType entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(GUIManager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
