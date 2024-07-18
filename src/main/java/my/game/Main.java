package my.game;

import javax.swing.JFrame;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Main
{   
    public static void main(String[] args)
    {
        Master master = new Master();
        
        master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        master.setTitle("The Global Empire");
        master.setResizable(false);

        InvitationContentPane invitationPanel = new InvitationContentPane(master);
        master.setContentPane(invitationPanel);
        master.pack();
        
        master.setVisible(true);
        master.setLocationRelativeTo(null);
    }
}
