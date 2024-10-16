package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Scaler
{
    private final double slope;
    private final double intercept;

    public Scaler(double xMin, double xMax, double yMin, double yMax)
    {
        slope = (yMax - yMin) / (xMax - xMin);
        intercept = yMin - slope * xMin;
    }

    public double transform(double x)
    {
        return slope * x + intercept;
    }
}
