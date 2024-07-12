package my.player;

import javax.swing.JButton;
import javax.swing.JPanel;
import my.game.Master;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserPlayer extends AbstractPlayer
{
    private final JPanel userPanel;
    
    public UserPlayer()
    {
        super(PlayerType.USER);
        
        userPanel = new JPanel();
        
        JButton button = new JButton("Done!");
        button.setActionCommand("done");
        button.addActionListener(Master.getInstance());
        userPanel.add(button);
    }
    
    @Override
    public void setColor(PlayerColor color)
    {
        super.setColor(color);
        userPanel.setBackground(color.colorValue);
    }
    
    public JPanel getUserPanel()    
    {
        return userPanel;
    }
}
