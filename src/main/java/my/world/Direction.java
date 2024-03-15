/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package my.world;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum Direction
{
    UP,
    RIGHT_UP,
    RIGHT_DOWN,
    DOWN,
    LEFT_DOWN,
    LEFT_UP;

    public Direction next()
    {
        Direction result = null;
        switch (this)
        {
            case UP ->
                result = RIGHT_UP;
            case RIGHT_UP ->
                result = RIGHT_DOWN;
            case RIGHT_DOWN ->
                result = DOWN;
            case DOWN ->
                result = LEFT_DOWN;
            case LEFT_DOWN ->
                result = LEFT_UP;
            case LEFT_UP ->
                result = UP;
        }
        return result;
    }
    
    public Direction prev()
    {
        Direction result = null;
        switch (this)
        {
            case UP ->
                result = LEFT_UP;
            case LEFT_UP ->
                result = LEFT_DOWN;
            case LEFT_DOWN ->
                result = DOWN;
            case DOWN ->
                result = RIGHT_DOWN;
            case RIGHT_DOWN ->
                result = RIGHT_UP;
            case RIGHT_UP ->
                result = UP;
        }
        return result;
    }
    
    public Direction opposite()
    {
        Direction result = null;
        switch (this)
        {
            case UP ->
                result = DOWN;
            case RIGHT_UP ->
                result = LEFT_DOWN;
            case RIGHT_DOWN ->
                result = LEFT_UP;
            case DOWN ->
                result = UP;
            case LEFT_DOWN ->
                result = RIGHT_UP;
            case LEFT_UP ->
                result = RIGHT_DOWN;
        }
        return result;
    }
}
