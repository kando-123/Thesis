package ge.view;

import ge.entity.*;
import ge.field.*;
import ge.main.*;
import ge.player.*;
import ge.utilities.*;
import ge.view.procedure.*;
import ge.world.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ViewManager
{
    private final JFrame frame;

    private UserPanel userPanel;
    private WorldPanel worldPanel;
    private WorldAccessor accessor;

    private Invoker<GameplayManager> invoker;
    private PlayersAccessor players;

    private Procedure procedure;
    
    private final Invoker<Engine> engineInvoker;

    public ViewManager(JFrame frame, Invoker<Engine> engineInvoker)
    {
        this.frame = frame;
        this.engineInvoker = engineInvoker;
    }

    public void makeView(WorldRenderer renderer, WorldAccessor accessor)
    {
        this.accessor = accessor;
        
        var contentPane = new JPanel(new BorderLayout());

        userPanel = new UserPanel(new Invoker<>(this));
        worldPanel = new WorldPanel(renderer, accessor, new Invoker<>(this));

        contentPane.add(userPanel, BorderLayout.WEST);
        contentPane.add(worldPanel, BorderLayout.CENTER);

        var toolkit = Toolkit.getDefaultToolkit();
        var size = toolkit.getScreenSize();
        size.width *= 0.75;
        size.height *= 0.75;
        contentPane.setPreferredSize(size);

        frame.addKeyListener(worldPanel.getInputHandler());

        frame.setContentPane(contentPane);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.requestFocus();
    }

    public void setInvoker(Invoker<GameplayManager> invoker)
    {
        this.invoker = invoker;
    }
    
    public void setPlayers(PlayersAccessor players)
    {
        this.players = players;
    }

    public void start()
    {
        var thread = new Thread(worldPanel);
        thread.setDaemon(true);
        thread.start();
    }

    void setUser(UserAccessor accessor)
    {
        userPanel.setBackground(accessor.getColor().rgb);
        userPanel.setUserName(accessor.getName());
        userPanel.setUserMoney(accessor.getMoney());
        userPanel.resetShop();
        
        if (worldPanel != null && worldPanel.getInputHandler().moveCenter())
        {
            worldPanel.setCenter(accessor.getCenter());
        }
    }

    void showBuildingInfo(BuildingType building)
    {
        var info = new BuildingInfoDialog(frame, building);
        info.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.requestFocus();
            }
        });
        info.setVisible(true);
    }

    void beginBuildingProcedure(BuildingType building)
    {
        if (procedure != null && procedure.status() == Procedure.Status.ONGOING)
        {
            procedure.rollback();
        }

        var current = players.current();

        assert (current instanceof UserPlayer);

        var user = (UserPlayer) current;
        procedure = new BuildingProcedure(building, user, invoker);
        procedure.advance(frame);
    }

    void showEntityInfo(EntityType entity)
    {
        var info = new EntityInfoDialog(frame, entity);
        info.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.requestFocus();
            }
        });
        info.setVisible(true);
    }

    void beginHiringProcedure(EntityType entity)
    {
        if (procedure != null && procedure.status() == Procedure.Status.ONGOING)
        {
            procedure.rollback();
        }

        var current = players.current();

        assert (current instanceof UserPlayer);

        var user = (UserPlayer) current;
        procedure = new HiringProcedure(entity, user, invoker);
        procedure.advance(frame);
    }

    void handleClick(Field field)
    {
        if (procedure != null)
        {
            if (field != null)
            {
                try
                {
                    procedure.advance(field);
                    procedure = null;
                }
                catch (Procedure.ProcedureException p)
                {
                    
                }
            }
            else
            {
                procedure.rollback();
                procedure = null;
            }
        }
        else if (field != null && field.isOccupied() && field.isOwned(players.current()))
        {
            procedure = new MovingProcedure(accessor, invoker);
            procedure.advance(field);
        }
    }
    
    void handleShiftClick(Field field)
    {
        if (procedure != null)
        {
            procedure.rollback();
            procedure = null;
        }
        if (field.isOccupied()
                && field.getEntity().canExtract(field.getHex(), accessor)
                && field.isOwned(players.current()))
        {
            procedure = new ExtractingProcedure(field, invoker, accessor);
            procedure.advance(frame);
        }
        // Repair and upgrade a fortification.
    }

    void finish()
    {
        if (procedure != null)
        {
            procedure.rollback();
            procedure = null;
        }
        frame.requestFocus();
        invoker.invoke(new NextPlayerCommand());
    }

    void updateMoney(int newAmount)
    {
        userPanel.setUserMoney(newAmount);
    }
    
    void showVictoryMessage(String name)
    {
        JOptionPane.showMessageDialog(frame, name.concat(" wins and becomes The Global Emperor."));
        engineInvoker.invoke(new ResetCommand());
    }
    
    void showHumanityLossMessage()
    {
        JOptionPane.showMessageDialog(frame, "All human players lost. AI took over the world.");
        engineInvoker.invoke(new ResetCommand());
    }
}
