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
    LOCAL("Local",
            "Tryb lokalny"),
    
    REMOTE_HOST("Remote: Host",
            "Tryb zdalny: Gospodarz"),
    
    REMOTE_GUEST("Remote: Guest",
            "Tryb zdalny: Gość"),
    
    LANGUAGE("Language",
            "Język"),
    
    READY("Ready",
            "Gotowe");

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
