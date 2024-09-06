package my.utils;

/**
 *
 * @author Kay Jay O'Nail
 */
public class IntegerLinearFunction
{
    private final int slope;

    private final int intercept;

    public IntegerLinearFunction(int slope, int intercept)
    {
        this.slope = slope;
        this.intercept = intercept;
    }

    public int computeFor(int x)
    {
        return slope * x + intercept;
    }
}
