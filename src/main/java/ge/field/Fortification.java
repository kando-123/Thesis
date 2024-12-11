package ge.field;

/**
 *
 * @author Kay Jay O'Nail
 */
public interface Fortification
{
    public int getFortitude();
    
    public static class CannotUpgradeException extends Exception
    {
    }
    public abstract boolean isDamaged();
    public abstract boolean isMaximal();
    public abstract void upgrade() throws CannotUpgradeException;
}
