package ge.entity;

import ge.view.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class EntityButton extends JButton
{
    private static final Color BACKGROUND_COLOR = Color.getHSBColor(210f / 360f, 0.3f, 1.0f);

    public EntityButton(EntityType entity, Invoker<ViewManager> invoker)
    {
        super(EntityAssetManager.getInstance().getIcon(entity.resource));
        
        setBackground(BACKGROUND_COLOR);
        setToolTipText(entity.toString());
        
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    invoker.invoke(new BeginHiringCommand(entity));
                }
                else if (e.getButton() == MouseEvent.BUTTON3)
                {
                    invoker.invoke(new EntityInfoCommand(entity));
                }
            }
        });
    }
}
