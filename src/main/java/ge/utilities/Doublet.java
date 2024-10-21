package ge.utilities;

import java.util.Objects;

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
    
    public Doublet()
    {
        this(null, null);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null || other.getClass() != Doublet.class)
        {
            return false;
        }
        try
        {
            Doublet that = (Doublet) other;
            
            return Objects.equals(this.left, that.left) && Objects.equals(this.right, that.right);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.left);
        hash = 71 * hash + Objects.hashCode(this.right);
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Doublet(left, right);
    }
}