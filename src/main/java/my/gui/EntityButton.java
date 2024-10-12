package my.gui;

import my.flow.Manager;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import my.command.BeginHiringCommand;
import my.command.EntityInfoCommand;
import my.command.Command;
import my.command.Invoker;
import my.unit.AbstractEntity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityButton extends JButton
{
    public EntityButton(Invoker<Manager> invoker, AbstractEntity entity)
    {
        super(entity.getIcon());
        
        setBackground(Color.getHSBColor(210f / 360f, 0.3f, 1.0f));
        
        setToolTipText(entity.getName());

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.isShiftDown())
                {
                    Command command = new EntityInfoCommand(entity);
                    invoker.invoke(command);
                }
                else
                {
                    Command command = new BeginHiringCommand(entity.copy());
                    invoker.invoke(command);
                }
            }
        });
    }
}