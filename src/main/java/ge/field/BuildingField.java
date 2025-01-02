package ge.field;

import ge.utilities.Hex;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class BuildingField extends Field
{
    protected BuildingField(Hex coords)
    {
        super(coords);
    }
    
    public static BuildingField newInstance(BuildingType type, Hex coords)
    {
        return switch (type)
        {
            case BARRACKS ->
            {
                yield new BarracksField(coords);
            }
            case FARM ->
            {
                yield new FarmField(coords);
            }
            case FORTRESS ->
            {
                yield new FortressField(coords);
            }
            case MINE ->
            {
                yield new MineField(coords);
            }
            case SHIPYARD ->
            {
                yield new ShipyardField(coords);
            }
            case TOWN ->
            {
                yield new TownField(coords);
            }
            case VILLAGE ->
            {
                yield new VillageField(coords);
            }
        };
    }
    
    public abstract BuildingType type();
}
