package my.gameplay.activity;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Activity
{
    private ActivityType type;
    
    protected Activity(ActivityType type)
    {
        this.type = type;
    }
    
    public ActivityType getType()
    {
        return type;
    }
}
