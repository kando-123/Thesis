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
    
    private Invoker<GameplayManager> invoker;
    private PlayersAccessor players;
    
    private Procedure procedure;
    
    public ViewManager(JFrame frame)
    {
        this.frame = frame;
    }
    
    public void makeView(WorldRenderer renderer, WorldAccessor accessor)
    {
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
        
        frame.addKeyListener(worldPanel.getKeyListener());
        
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
        if (procedure != null)
        {
            procedure.rollback();
        }
        
        var current = players.current();
        
        assert (current instanceof UserPlayer);
        
        var user = (UserPlayer) current;
        procedure = new BuildingProcedure(building, user, new Invoker<>(this));
        procedure.advance(frame);
    }
    
    void finishBuilding(BuildingField building)
    {
        invoker.invoke(new BuildCommand(building));
        procedure = null;
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
        if (procedure != null)
        {
            procedure.rollback();
        }
        
        var current = players.current();
        
        assert (current instanceof UserPlayer);
        
        var user = (UserPlayer) current;
        procedure = new HiringProcedure(entity, user, new Invoker<>(this));
        procedure.advance(frame);
    }
    
    void handleClick(Field field)
    {
        if (procedure != null)
        {
            procedure.advance(field);
        }
    }
    
    void finish()
    {
        frame.requestFocus();
        invoker.invoke(new NextPlayerCommand());
    }
    
    void updateMoney(int newAmount)
    {
        userPanel.setUserMoney(newAmount);
    }
    
    void focus()
    {
        frame.requestFocus();
    }
}
