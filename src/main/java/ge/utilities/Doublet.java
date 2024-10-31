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
    public E x;
    public E y;
    
    public Doublet(E x, E y)
    {
        this.x = x;
        this.y = y;
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
            
            return Objects.equals(this.x, that.x) && Objects.equals(this.y, that.y);
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
        hash = 71 * hash + Objects.hashCode(this.x);
        hash = 71 * hash + Objects.hashCode(this.y);
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Doublet(x, y);
    }
}