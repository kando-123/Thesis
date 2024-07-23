package my.utils;

/**
 *
 * @param <E>
 * 
 * @author Kay Jay O'Nail
 */
public class Doublet<E>
{
    public E left;
    public E right;
    
    public Doublet(E left, E right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Doublet(left, right);
    }
}
