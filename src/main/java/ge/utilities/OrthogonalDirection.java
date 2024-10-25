package ge.utilities;

public enum OrthogonalDirection
{
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST,
    NORTH,
    NORTHEAST;

    public OrthogonalDirection opposite()
    {
        var values = values();
        int length = values.length;
        int index = (ordinal() + (length / 2)) % length;
        return values[index];
    }
}
