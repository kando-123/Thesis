package my.flow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import my.command.Invoker;
import my.entity.AbstractEntity;
import my.field.AbstractField;
import my.gui.EntityInfoDialog;
import my.gui.EntityPurchaseDialog;
import my.gui.Master;
import my.player.Player;
import my.world.WorldMarker;
import my.world.WorldMutator;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityManager
{
    private final Master master;
    private final WorldMarker marker;
    private final WorldMutator mutator;

    private AbstractEntity entity;
    private Player player;

    private EntityPurchaseDialog dialog;

    public EntityManager(Master master, WorldMarker marker, WorldMutator mutator)
    {
        this.master = master;
        this.marker = marker;
        this.mutator = mutator;
    }

    public void showInfo(AbstractEntity entity)
    {
        var info = new EntityInfoDialog(master, entity);
        info.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                master.requestFocus();
            }
        });
        info.setVisible(true);
    }

    public void begin(AbstractEntity entity, Player player, Invoker<Manager> invoker)
    {
        if (!player.canHire(entity))
        {
            JOptionPane.showMessageDialog(master, """
                        Unfortunately, you cannot buy this entity.

                        You do not have a place for this entity.
                        Shift-click the entity's button for details.""");
            master.requestFocus();
        }
        else
        {
            this.entity = entity;
            this.player = player;

            var builder = new EntityPurchaseDialog.Builder();
            builder.setFrame(master);
            builder.setInvoker(invoker);
            builder.setEntity(entity);
            builder.setBudget(player.getMoney());
            dialog = builder.get();
            dialog.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    master.requestFocus();
                }
            });
            dialog.setVisible(true);
        }
    }

    public void pursue()
    {
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;

        player.markFor(entity);

        master.requestFocus();
    }

    public void finish(AbstractField field)
    {
        hire(entity, field);
        entity = null;
        player = null;
    }
    
    public void hire(AbstractEntity entity, AbstractField field)
    {
        if (field != null && marker.isMarked(field.getHex()))
        {
            master.setMoney(player.buy(entity));
            field.pin(entity);

            if (field.isCapital())
            {
                entity.setMovable(true);
            }
        }
        marker.unmarkAll();
        master.requestFocus();
    }
}
