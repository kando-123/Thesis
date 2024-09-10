package my.utils;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Hex
{
    private int pCoord;
    private int qCoord;
    private int rCoord;

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
    
    public static Hex getHexAt(int x, int y, int outerRadius)
    {
        int p = (int) ((2. / 3.) * x / outerRadius);
        int q = (int) ((-x + Math.sqrt(3.) * y) / (3. * outerRadius));
        
        return new Hex(p, q, -(p + q));
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

    /* Mutators */
    
    public void add(Hex other)
    {
        pCoord += other.pCoord;
        qCoord += other.qCoord;
        rCoord += other.rCoord;
    }

    public void subtract(Hex other)
    {
        pCoord -= other.pCoord;
        qCoord -= other.qCoord;
        rCoord -= other.rCoord;
    }

    public void multiply(int factor)
    {
        pCoord *= factor;
        qCoord *= factor;
        rCoord *= factor;
    }

    public void shift(HexagonalDirection direction)
    {
        switch (direction)
        {
            case UP ->
            {
                --qCoord;
                ++rCoord;
            }
            case RIGHT_UP ->
            {
                ++pCoord;
                --qCoord;
            }
            case RIGHT_DOWN ->
            {
                ++pCoord;
                --rCoord;
            }
            case DOWN ->
            {
                ++qCoord;
                --rCoord;
            }
            case LEFT_DOWN ->
            {
                --pCoord;
                ++qCoord;
            }
            case LEFT_UP ->
            {
                --pCoord;
                ++rCoord;
            }
        }
    }

    public void rotateRight()
    {
        int p = -qCoord;
        int q = -rCoord;
        int r = -pCoord;
        pCoord = p;
        qCoord = q;
        rCoord = r;
    }

    public void rotateLeft()
    {
        int p = -rCoord;
        int q = -pCoord;
        int r = -qCoord;
        pCoord = p;
        qCoord = q;
        rCoord = r;
    }

    public void reverse()
    {
        pCoord = -pCoord;
        qCoord = -qCoord;
        rCoord = -rCoord;
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

    public Hex rotatedRight()
    {
        return new Hex(-qCoord, -rCoord, -pCoord);
    }

    public Hex rotatedLeft()
    {
        return new Hex(-rCoord, -pCoord, -qCoord);
    }

    public Hex opposite()
    {
        return new Hex(-pCoord, -qCoord, -rCoord);
    }

    public Hex neighbor(HexagonalDirection direction)
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
        HexagonalDirection[] directions = HexagonalDirection.values();
        
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

    public boolean isRadial()
    {
        return pCoord == 0 || qCoord == 0 || rCoord == 0;
    }

    public int getRing()
    {
        return (Math.abs(pCoord) + Math.abs(qCoord) + Math.abs(rCoord)) / 2;
    }
    
    /* Geometry */
    
    public static int getHexSurfaceSize(int side)
    {
        return 3 * side * (side - 1) + 1;
    }
    
    public Doublet<Integer> getCentralPoint(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2;
        int y = (qCoord - rCoord) * innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Integer> getCornerPoint(int outerRadius, int innerRadius)
    {
        int x = pCoord * outerRadius * 3 / 2 - outerRadius;
        int y = (qCoord - rCoord) * innerRadius - innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Double> getCentralPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5;
        double y = (double) (qCoord - rCoord) * innerRadius;
        return new Doublet<>(x, y);
    }

    public Doublet<Double> getCornerPoint(double outerRadius, double innerRadius)
    {
        double x = (double) (pCoord) * outerRadius * 1.5 - outerRadius;
        double y = (double) (qCoord - rCoord) * innerRadius - innerRadius;
        return new Doublet<>(x, y);
    }

    public static Doublet<Integer> computeCentralPointAt(int p, int q, int r, int outerRadius, int innerRadius)
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

    public static Doublet<Integer> computeCornerPointAt(int p, int q, int r, int outerRadius, int innerRadius)
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

    public static Doublet<Double> computeCentralPointAt(int p, int q, int r, double outerRadius, double innerRadius)
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

    public static Doublet<Double> computeCornerPointAt(int p, int q, int r, double outerRadius, double innerRadius)
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
    
    public static int computeSurfaceWidth(int side, int outerRadius)
    {
        return 3 * (side - 1) * outerRadius + 2 * outerRadius;
    }
    
    public static int computeSurfaceHeight(int side, int innerRadius)
    {
        return 4 * (side - 1) * innerRadius + 2 * innerRadius;
    }
    
    public static double computeSurfaceWidth(int side, double outerRadius)
    {
        return 3 * (side - 1) * outerRadius + 2 * outerRadius;
    }
    
    public static double computeSurfaceHeight(int side, double innerRadius)
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
    
    private static final double SQRT_3 = Math.sqrt(3.0);
    
    public static Hex getHexAt(double x, double y, double outerRadius, double innerRadius)
    {
        double fractionalP = (2./3. * x) / outerRadius;
        double fractionalQ = (-1./3. * x + SQRT_3/3. * y) / outerRadius;
        double fractionalR = -(fractionalP + fractionalQ);
        
        long p = Math.round(fractionalP);
        long q = Math.round(fractionalQ);
        long r = Math.round(fractionalR);
        
        double Δp = Math.abs(p - fractionalP);
        double Δq = Math.abs(q - fractionalQ);
        double Δr = Math.abs(r - fractionalR);
        
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
}
