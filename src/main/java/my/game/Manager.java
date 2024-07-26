package my.game;

import java.util.*;
import javax.swing.*;
import my.units.*;

class PropertiesFrame extends JFrame
{
    private final List<FieldType> properties;
    
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel conditionsLabel;
    private JLabel priceLabel;
    
    public PropertiesFrame()
    {
        properties = new ArrayList<>();
        for (var fieldType : FieldType.values())
        {
            if (fieldType.isPurchasable())
            {
                properties.add(fieldType);
            }
        }
        
        setContentPane(makeContentPane());
    }
    
    private JPanel makeContentPane()
    {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        
        
        
        FieldsManager fieldsManager = FieldsManager.getInstance();
        
        
        
        return contentPane;
    }
}


/**
 *
 * @author Kay Jay O'Nail
 */
public class Manager
{

}
