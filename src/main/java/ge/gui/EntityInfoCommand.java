package ge.gui;

import ge.entity.*;
import ge.utilities.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityInfoCommand extends Command<GUIManager>
{
    private final EntityType entity;

    public EntityInfoCommand(EntityType entity)
    {
        this.entity = entity;
    }
    
    @Override
    public void execute(GUIManager executor)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
