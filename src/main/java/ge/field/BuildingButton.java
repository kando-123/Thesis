package ge.field;

import ge.view.*;
import ge.utilities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingButton extends JButton
{
    private static final Color BACKGROUND_COLOR = Color.getHSBColor(210f / 360f, 0.3f, 1.0f);
    
    public BuildingButton(BuildingType building, Invoker<ViewManager> invoker)
    {
        super(FieldAssetManager.getInstance().getIcon(building.resource));

        setBackground(BACKGROUND_COLOR);
        setToolTipText(building.toString());

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.isShiftDown())
                {
                    invoker.invoke(new BuildingInfoCommand(building));
                }
                else
                {
                    invoker.invoke(new BeginBuildingCommand(building));
                }
            }
        });
    }
}
