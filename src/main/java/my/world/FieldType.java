/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package my.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum FieldType
{
    BARRACKS("/Fields/Barracks.png"),
    CAPITAL("/Fields/Capital.png"),
    FARMFIELD("/Fields/Farmfield.png"),
    LAND_1("/Fields/Land1.png"),
    LAND_2("/Fields/Land2.png"),
    MINE("/Fields/Mine.png"),
    MOUNTS("/Fields/Mounts.png"),
    SEE_1("/Fields/See1.png"),
    SEE_2("/Fields/See2.png"),
    SHIPYARD("/Fields/Shipyard.png"),
    TOWN("/Fields/Town.png"),
    VILLAGE("/Fields/Village.png"),
    WOOD("/Fields/Wood.png");
    
    public final String path;
    
    private FieldType(String path)
    {
        this.path = path;
    }
}
