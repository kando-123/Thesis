package ge.utilities;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Hex
{
    private final int pCoord;
    private final int qCoord;
    private final int rCoord;

    /* Creation of a new instance */
    private Hex(int p, int q, int r)
    {
        assert (p + q + r == 0);

        pCoord = p;
        qCoord = q;
        rCoord = r;
    }

    @Override
    public Hex clone()
    {
        return new Hex(pCoord, qCoord, rCoord);
    }

    public static Hex newInstance(int p, int q)
    {
        return new Hex(p, q, -(p + q));
    }

    public static Hex newInstance(int p, int q, int r)
    {
        return (p + q + r == 0) ? new Hex(p, q, r) : null;
    }

    public static Hex getOrigin()
    {
        return new Hex(0, 0, 0);
    }

    public static Hex at(double x, double y, double outerRadius, double innerRadius)
    {
        // Fractional coordinates
        double Φp = (2.0 / 3.0 * x) / outerRadius;
        double Φq = (-1.0 / 3.0 * x + Math.sqrt(3.0) / 3.0 * y) / outerRadius;
        double Φr = -(Φp + Φq);

        long p = Math.round(Φp);
        long q = Math.round(Φq);
        long r = Math.round(Φr);

        double Δp = Math.abs(p - Φp);
        double Δq = Math.abs(q - Φq);
        double Δr = Math.abs(r - Φr);

        if (Δp > Δq && Δp > Δr)
        {
            p = -(q + r);
        }
        else if (Δq > Δr && Δq > Δp)
        {
            q = -(r + p);
        }
        else // if (Δr > Δp && Δr > Δq)
        {
            r = -(p + q);
        }

        return new Hex((int) p, (int) q, (int) r);
    }
    
    public static Hex at(double x, double y)
    {
        // Fractional coordinates
        double Φp = (2.0 / 3.0 * x);
        double Φq = (-1.0 / 3.0 * x + Math.sqrt(3.0) / 3.0 * y);
        double Φr = -(Φp + Φq);

        long p = Math.round(Φp);
        long q = Math.round(Φq);
        long r = Math.round(Φr);

        double Δp = Math.abs(p - Φp);
        double Δq = Math.abs(q - Φq);
        double Δr = Math.abs(r - Φr);

        if (Δp > Δq && Δp > Δr)
        {
            p = -(q + r);
        }
        else if (Δq > Δr && Δq > Δp)
        {
            q = -(r + p);
        }
        else // if (Δr > Δp && Δr > Δq)
        {
            r = -(p + q);
        }

        return new Hex((int) p, (int) q, (int) r);
    }

    /* Property accessors */
    public int getP()
    {
        return pCoord;
    }

    public int getQ()
    {
        return qCoord;
    }

    public int getR()
    {
        return rCoord;
    }
    
    public double getX()
    {
        return 1.5 * pCoord;
    }
    
    public double getY()
    {
        return (qCoord - rCoord) * INNER_TO_OUTER_RATIO;
    }

    /* Operators */
    public Hex plus(Hex other)
    {
        int p = pCoord + other.pCoord;
        int q = qCoord + other.qCoord;
        int r = rCoord + other.rCoord;
        return new Hex(p, q, r);
    }

    public Hex minus(Hex other)
    {
        int p = pCoord - other.pCoord;
        int q = qCoord - other.qCoord;
        int r = rCoord - other.rCoord;
        return new Hex(p, q, r);
    }

    public Hex times(int factor)
    {
        int p = pCoord * factor;
        int q = qCoord * factor;
        int r = rCoord * factor;
        return new Hex(p, q, r);
    }

    public Hex neighbor(Direction direction)
    {
        int p = pCoord;
        int q = qCoord;
        int r = rCoord;
        switch (direction)
        {
            case UP ->
            {
                --q;
                ++r;
            }
            case RIGHT_UP ->
            {
                ++p;
                --q;
            }
            case RIGHT_DOWN ->
            {
                ++p;
                --r;
            }
            case DOWN ->
            {
                ++q;
                --r;
            }
            case LEFT_DOWN ->
            {
                --p;
                ++q;
            }
            case LEFT_UP ->
            {
                --p;
                ++r;
            }
        }
        return new Hex(p, q, r);
    }

    public Hex[] neighbors()
    {
        Direction[] directions = Direction.values();

        Hex[] hexes = new Hex[directions.length];
        for (int i = 0; i < directions.length; ++i)
        {
            hexes[i] = neighbor(directions[i]);
        }

        return hexes;
    }

    /* Descriptors */
    public int distance(Hex other)
    {
        int pDistance = Math.abs(pCoord - other.pCoord);
        int qDistance = Math.abs(qCoord - other.qCoord);
        int rDistance = Math.abs(rCoord - other.rCoord);
        return (pDistance + qDistance + rDistance) / 2;
    }

    public boolean isNeighbor(Hex other)
    {
        return distance(other) == 1;
    }

    public boolean isRadial()
    {
        return pCoord == 0 || qCoord == 0 || rCoord == 0;
    }

    public int getRing()
    {
        return (Math.abs(pCoord) + Math.abs(qCoord) + Math.abs(rCoord)) / 2;
    }

    /* Geometry */
    private static final double INNER_TO_OUTER_RATIO = Math.sin(Math.PI / 3.0);

    public static double innerRadius(double outerRadius)
    {
        return outerRadius * INNER_TO_OUTER_RATIO;
    }

    public static int surfaceSize(int side)
    {
        return 3 * side * (side - 1) + 1;
    }

    public Doublet<Integer> centralPoint(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2;
        int y = (qCoord - rCoord) * innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Integer> cornerPoint(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2 - outerRadius;
        int y = (qCoord - rCoord) * innerRadius - innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Double> centralPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5;
        double y = (double) (qCoord - rCoord) * innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Double> cornerPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5 - outerRadius;
        double y = (double) (qCoord - rCoord) * innerRadius - innerRadius;
        return new Doublet<>(x, y);
    }

    public static Doublet<Integer> centralPointAt(int p, int q, int r, int outerRadius, int innerRadius)
    {
        Doublet<Integer> point = null;
        if (p + q + r == 0)
        {
            int x = p * outerRadius * 3 / 2;
            int y = (q - r) * innerRadius;
            point = new Doublet<>(x, y);
        }
        return point;
    }

    public static Doublet<Integer> cornerPointAt(int p, int q, int r, int outerRadius, int innerRadius)
    {
        Doublet<Integer> point = null;
        if (p + q + r == 0)
        {
            int x = p * outerRadius * 3 / 2 - outerRadius;
            int y = (q - r) * innerRadius - innerRadius;
            point = new Doublet<>(x, y);
        }
        return point;
    }

    public static Doublet<Double> centralPointAt(int p, int q, int r, double outerRadius, double innerRadius)
    {
        Doublet<Double> point = null;
        if (p + q + r == 0)
        {
            double x = (double) (p) * outerRadius * 1.5;
            double y = (double) (q - r) * innerRadius;
            point = new Doublet<>(x, y);
        }
        return point;
    }

    public static Doublet<Double> cornerPointAt(int p, int q, int r, double outerRadius, double innerRadius)
    {
        Doublet<Double> point = null;
        if (p + q + r == 0)
        {
            double x = (double) (p) * outerRadius * 1.5 - outerRadius;
            double y = (double) (q - r) * innerRadius - innerRadius;
            point = new Doublet<>(x, y);
        }
        return point;
    }

    public static int surfaceWidth(int side, int outerRadius)
    {
        return 3 * (side - 1) * outerRadius + 2 * outerRadius;
    }

    public static int surfaceHeight(int side, int innerRadius)
    {
        return 4 * (side - 1) * innerRadius + 2 * innerRadius;
    }

    public static double surfaceWidth(int side, double outerRadius)
    {
        return 3 * (side - 1) * outerRadius + 2 * outerRadius;
    }

    public static double surfaceHeight(int side, double innerRadius)
    {
        return 4 * (side - 1) * innerRadius + 2 * innerRadius;
    }

    /* Other */
    @Override
    public boolean equals(Object other)
    {
        if (other.getClass() == Hex.class)
        {
            Hex hex = (Hex) other;
            return pCoord == hex.pCoord && qCoord == hex.qCoord && rCoord == hex.rCoord;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + this.pCoord;
        hash = 19 * hash + this.qCoord;
        hash = 19 * hash + this.rCoord;
        return hash;
    }

    @Override
    public String toString()
    {
        return String.format("Hex@[p=%d, q=%d, r=%d]", pCoord, qCoord, rCoord);
    }

    public enum Direction
    {
        UP,
        RIGHT_UP,
        RIGHT_DOWN,
        DOWN,
        LEFT_DOWN,
        LEFT_UP;

        public Direction next()
        {
            var values = values();
            var index = (ordinal() + 1) % values.length;
            return values[index];
        }

        public Direction prev()
        {
            var values = values();
            int length = values.length;
            int index = (ordinal() + length - 1) % length;
            return values[index];
        }

        public Direction opposite()
        {
            var values = values();
            int length = values.length;
            int index = (ordinal() + (length / 2)) % length;
            return values[index];
        }
    }
}
