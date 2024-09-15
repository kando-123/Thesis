package my.gui;

import my.flow.Manager;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import my.command.BeginBuildingCommand;
import my.command.BuildingInfoCommand;
import my.command.Command;
import my.command.Invoker;
import my.field.BuildingField;

/**
 *
 * @author Kay Jay O'Nail
 */
public class BuildingButton extends JButton
{
    public BuildingButton(Invoker<Manager> invoker, BuildingField building)
    {
        super(building.getIcon());
        
        setBackground(Color.getHSBColor(210f / 360f, 0.3f, 1.0f));
        setToolTipText(building.getName());

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.isShiftDown())
                {
                    Command command = new BuildingInfoCommand(building);
                    invoker.invoke(command);
                }
                else
                {
                    Command command = new BeginBuildingCommand((BuildingField) building.copy());
                    invoker.invoke(command);
                }
            }
        });
    }
}
