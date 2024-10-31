package ge.player;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class UserAccessor
{
    private final UserPlayer user;

    public UserAccessor(UserPlayer user)
    {
        this.user = user;
    }
    
    public String getName()
    {
        return user.getName();
    }
    
    public int getMoney()
    {
        return user.getMoney();
    }
    
    public Player.ContourColor getColor()
    {
        return user.getColor();
    }
    
    public Hex getCenter()
    {
        return user.center();
    }
}
