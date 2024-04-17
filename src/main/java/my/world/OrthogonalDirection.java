package my.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum OrthogonalDirection
{
    EAST(-OrthogonalDirection.unitOffset, 0),
    SOUTH(0, -OrthogonalDirection.unitOffset),
    WEST(+OrthogonalDirection.unitOffset, 0),
    NORTH(0, +OrthogonalDirection.unitOffset),
    
    SOUTHEAST(-OrthogonalDirection.unitDiagonal, -OrthogonalDirection.unitDiagonal),
    SOUTHWEST(+OrthogonalDirection.unitDiagonal, -OrthogonalDirection.unitDiagonal),
    NORTHWEST(+OrthogonalDirection.unitDiagonal, +OrthogonalDirection.unitDiagonal),
    NORTHEAST(-OrthogonalDirection.unitDiagonal, +OrthogonalDirection.unitDiagonal);
    
    
    private static final int unitOffset = 6;
    private static final int unitDiagonal = 4;
    
    private final Pixel offset;
    
    private OrthogonalDirection(int xOffset, int yOffset)
    {
        offset = new Pixel(xOffset, yOffset);
    }
    
    public Pixel getOffset()
    {
        return offset;
    }
}
