package ge.main;

import ge.player.Player;
import ge.utilities.Command;
import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public class AdjustMoraleCommand extends Command<GameplayManager>
{
    private final Hex coords;
    private final Player winner;
    private final Player loser;

    public AdjustMoraleCommand(Hex coords, Player winner, Player loser)
    {
        this.coords = coords;
        this.winner = winner;
        this.loser = loser;
    }
    
    @Override
    public void execute(GameplayManager executor)
    {
        executor.adjustMorale(coords, winner, loser);
    }
}
