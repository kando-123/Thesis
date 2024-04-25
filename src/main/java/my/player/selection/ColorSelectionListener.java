package my.player.selection;

/**
 *
 * @author Kay Jay O'Nail
 */
public interface ColorSelectionListener
{
    public void colorSelected(PlayerColor color, ColorModel skip);
    public void colorDeselected(PlayerColor color, ColorModel skip);
}
