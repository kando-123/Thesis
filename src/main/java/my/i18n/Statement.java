package my.i18n;

/**
 *
 * @author Kay Jay O'Nail
 */
public enum Statement
{   
    PLAY("Play",
        "Graj"),
    
    JOIN("Join",
        "Dołącz"),
    
    LANGUAGE("Language",
            "Język"),
    
    READY("Ready",
            "Gotowe");

    private final String inEnglish;
    private final String inPolish;

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
