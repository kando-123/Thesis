package my.main;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import my.command.BeginHiringCommand;
import my.command.EntityInfoCommand;
import my.command.Command;
import my.entity.AbstractEntity;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityButton extends JButton
{
    public EntityButton(Manager manager, AbstractEntity entity)
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
                    manager.passCommand(command);
                }
                else
                {
                    Command command = new BeginHiringCommand(entity);
                    manager.passCommand(command);
                }
            }
        });
    }
}