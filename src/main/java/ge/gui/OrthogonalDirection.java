package ge.gui;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum OrthogonalDirection
{
    EAST,
    SOUTH,
    WEST,
    NORTH,
    
    SOUTHEAST,
    SOUTHWEST,
    NORTHWEST,
    NORTHEAST;
    
    public OrthogonalDirection opposite()
    {
        return switch (this)
        {
            case EAST ->
            {
                yield WEST;
            }
            case SOUTH ->
            {
                yield NORTH;
            }
            case WEST ->
            {
                yield EAST;
            }
            case NORTH ->
            {
                yield SOUTH;
            }
            case SOUTHEAST ->
            {
                yield NORTHWEST;
            }
            case SOUTHWEST ->
            {
                yield NORTHEAST;
            }
            case NORTHWEST ->
            {
                yield SOUTHEAST;
            }
            case NORTHEAST ->
            {
                yield SOUTHWEST;
            }
        };
    }
}
