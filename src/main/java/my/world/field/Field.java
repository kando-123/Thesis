/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world.field;

import java.awt.image.BufferedImage;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Field
{
    private final FieldType type;
    private final BufferedImage image;
    
    public Field(FieldType type)
    {
        this.type = type;
        FieldManager fieldManager = FieldManager.getInstance();
        image = fieldManager.getImage(type);
    }
    
    public FieldType getType()
    {
        return type;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
}
