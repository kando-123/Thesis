package my.entity;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum EntityType
{
    INFANTRY,
    CAVALRY,
    NAVY;

    public String getFile()
    {
        String name = name();
        return String.format("/Entities/%s%s.png",
                name.substring(0, 1).toUpperCase(),
                name.substring(1).toLowerCase());
    }

    public int getPrice()
    {
        return switch (this)
        {
            case INFANTRY ->
            {
                yield 20;
            }
            case CAVALRY ->
            {
                yield 50;
            }
            case NAVY ->
            {
                yield 100;
            }
        };
    }
}
