package my.i18n;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Dictionary
{
    private Language language;
    
    private Dictionary()
    {
        language = Language.ENGLISH;
    }
    
    public void setLanguage(Language newLanguage)
    {
        language = newLanguage;
    }
    
    public Language getLanguage()
    {
        return language;
    }
    
    public String translate(Statement statement)
    {
        String translation = "";
        switch (language)
        {
            case ENGLISH ->
            {
                translation = statement.toEnglish();
            }
            case POLISH ->
            {
                translation = statement.toPolish();
            }
        }
        return translation;
    }
    
    private static Dictionary instance = null;
    
    public static Dictionary getInstance()
    {
        if (instance == null)
        {
            instance = new Dictionary();
        }
        return instance;
    }
    
}