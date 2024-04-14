/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package my.i18n;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum Statement
{   
    SINGLEPLAYER("Singleplayer",
            "Tryb jednoosobowy"),
    
    MULTIPLAYER_HOST("Multiplayer: Host",
            "Tryb wieloosobowy: Gospodarz"),
    
    MULTIPLAYER_GUEST("Multiplayer: Guest",
            "Tryb wieloosobowy: Gość"),
    
    LANGUAGE("Language",
            "Język");

    private String inEnglish;
    private String inPolish;

    private Statement(String english, String polish)
    {
        inEnglish = english;
        inPolish = polish;
    }

    public String toEnglish()
    {
        return inEnglish;
    }

    public String toPolish()
    {
        return inPolish;
    }
}
