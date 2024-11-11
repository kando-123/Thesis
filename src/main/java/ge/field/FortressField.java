package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class FortressField extends FortificationField
{
    private int level;
    private int fortitude;
    
    private static final int LEVELS = 3;
    private static final int FORTITUDE[] = { 100, 125, 150 };
    
    public FortressField(Hex coords)
    {
        super(coords);
        
        level = 0;
        fortitude = FORTITUDE[level];
    }

    @Override
    public int getFortitude()
    {
        return fortitude;
    }
    
    private static class CannotUpgradeException extends Exception
    {
    }
    
    private void upgrade() throws CannotUpgradeException 
    {
        if (level < LEVELS - 1)
        {
            fortitude = FORTITUDE[++level];
        }
        else
        {
            throw new CannotUpgradeException();
        }
    }

    @Override
    public BuildingType getType()
    {
        return BuildingType.FORTRESS;
    }
}
