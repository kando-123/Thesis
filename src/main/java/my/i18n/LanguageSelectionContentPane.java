package my.i18n;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.game.Master;

/**
 *
 * @author Kay Jay O'Nail
 */
public class LanguageSelectionContentPane extends JPanel implements ActionListener
{
    private final Master master;
    private Language language;

    public LanguageSelectionContentPane()
    {
        super(new GridBagLayout());

        master = Master.getInstance();

        Dictionary dictionary = Dictionary.getInstance();
        language = dictionary.getLanguage();

        GridBagConstraints c = new GridBagConstraints();
        JRadioButton english = new JRadioButton("English");
        english.setActionCommand("English");
        english.addActionListener(this);
        if (language == Language.ENGLISH)
        {
            english.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 10, 5, 10);
        add(english, c);

        //c = new GridBagConstraints();
        ++c.gridy;
        JRadioButton polish = new JRadioButton("Polski");
        polish.setActionCommand("Polish");
        polish.addActionListener(this);
        if (language == Language.POLISH)
        {
            polish.setSelected(true);
        }
//        c.gridx = 0;
//        c.gridy = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        add(polish, c);

//        c = new GridBagConstraints();
        ++c.gridy;
        JRadioButton german = new JRadioButton("Deutsch");
        german.setActionCommand("German");
        german.addActionListener(this);
        german.setEnabled(false);
        if (language == Language.GERMAN)
        {
            german.setSelected(true);
        }
//        c.gridx = 0;
//        c.gridy = 2;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        add(german, c);

//        c = new GridBagConstraints();
        ++c.gridy;
        JRadioButton spanish = new JRadioButton("Español");
        spanish.setActionCommand("Spanish");
        spanish.addActionListener(this);
        spanish.setEnabled(false);
        if (language == Language.SPANISH)
        {
            spanish.setSelected(true);
        }
//        c.gridx = 0;
//        c.gridy = 3;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        add(spanish, c);

//        c = new GridBagConstraints();
        ++c.gridy;
        JRadioButton italian = new JRadioButton("Italiano");
        italian.setActionCommand("Italian");
        italian.addActionListener(this);
        italian.setEnabled(false);
        if (language == Language.ITALIAN)
        {
            italian.setSelected(true);
        }
//        c.gridx = 0;
//        c.gridy = 4;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        add(italian, c);

//        c = new GridBagConstraints();
        ++c.gridy;
        JRadioButton french = new JRadioButton("Français");
        french.setActionCommand("French");
        french.addActionListener(this);
        french.setEnabled(false);
        if (language == Language.FRENCH)
        {
            french.setSelected(true);
        }
//        c.gridx = 0;
//        c.gridy = 5;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        add(french, c);

        ButtonGroup group = new ButtonGroup();
        group.add(english);
        group.add(polish);
        group.add(german);
        group.add(spanish);
        group.add(italian);
        group.add(french);

        c = new GridBagConstraints();
        String text = dictionary.translate(Statement.READY);
        JButton readyButton = new JButton(text);
        readyButton.setActionCommand("ready");
        readyButton.addActionListener(this);
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(readyButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "English" ->
            {
                language = Language.ENGLISH;
            }
            case "Polish" ->
            {
                language = Language.POLISH;
            }
            case "German" ->
            {
                language = Language.GERMAN;
            }
            case "Spanish" ->
            {
                language = Language.SPANISH;
            }
            case "Italian" ->
            {
                language = Language.ITALIAN;
            }
            case "French" ->
            {
                language = Language.FRENCH;
            }
            case "ready" ->
            {
                Dictionary dictionary = Dictionary.getInstance();
                dictionary.setLanguage(language);

                ActionEvent event = new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED,
                        "language-selected");
                master.actionPerformed(event);
            }
        }
    }
}
