package my.entity;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum EntityType
{
    INFANTRY(3, 2, 0),
    CAVALRY(4, 1, 0),
    NAVY(0, 0, 5);
    
    public final int plainsSpeed;
    public final int mountainsSpeed;
    public final int seaSpeed;
    
    private EntityType(int plainsSpeed, int mountainsSpeed, int seaSpeed)
    {
        this.plainsSpeed = plainsSpeed;
        this.mountainsSpeed = mountainsSpeed;
        this.seaSpeed = seaSpeed;
    }

    public String getFile()
    {
        String name = name();
        return String.format("/Entities/%s%s.png",
                name.substring(0, 1).toUpperCase(),
                name.substring(1).toLowerCase());
    }
    
    public String getIconFile()
    {
        String name = name();
        return String.format("/iEntities/i%s%s.png",
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
                yield 25;
            }
            case NAVY ->
            {
                yield 130;
            }
        };
    }
    
    public String getDescription()
    {
        return switch (this)
        {
            case INFANTRY ->
            {
                yield "Infantry can go up to %d plains field(s) and %d mountainous field(s) per round. "
                        .formatted(INFANTRY.plainsSpeed, INFANTRY.mountainsSpeed)
                    + "Infantry can embark and disembark ships.";
            }
            case CAVALRY ->
            {
                yield "Cavalry can go up to %d plains field(s) and %d mountainous field(s) per round. "
                        .formatted(CAVALRY.plainsSpeed, CAVALRY.mountainsSpeed);
            }
            case NAVY ->
            {
                yield "Navy can go up to %d sea field(s) per round. Navy transports Infantry as sailors."
                        .formatted(NAVY.seaSpeed);
            }
        };
    }
}
