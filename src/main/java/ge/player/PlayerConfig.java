package ge.player;

/**
 *
 * @author Kay Jay O'Nail
 */
public class PlayerConfig
{
    final PlayerType type;
    final PlayerColor color;
    final String name;

    public PlayerConfig(PlayerType type, PlayerColor color, String name)
    {
        this.type = type;
        this.color = color;
        this.name = name;
    }
}
