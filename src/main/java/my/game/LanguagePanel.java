/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.i18n.Dictionary;
import my.i18n.Language;
import my.i18n.Statement;

/**
 *
 * @author Kay Jay O'Nail
 */
public class LanguagePanel extends JPanel implements ActionListener
{
    private Master master;
    private Language language;
    
    public LanguagePanel(Master parentFrame)
    {
        super(new GridBagLayout());
        
        master = parentFrame;
        
        Dictionary dictionary = Dictionary.getInstance();
        language = dictionary.getLanguage();
        
        GridBagConstraints c = new GridBagConstraints();
        JRadioButton english = new JRadioButton("English");
        english.setActionCommand("English");
        english.addActionListener(this);
        if (language == language.ENGLISH)
        {
            english.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(english, c);
        
        c = new GridBagConstraints();
        JRadioButton polish = new JRadioButton("Polski");
        polish.setActionCommand("Polish");
        polish.addActionListener(this);
        if (language == language.POLISH)
        {
            polish.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(polish, c);
        
        c = new GridBagConstraints();
        JRadioButton german = new JRadioButton("Deutsch");
        german.setActionCommand("German");
        german.addActionListener(this);
        german.setEnabled(false);
        if (language == language.GERMAN)
        {
            german.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(german, c);
        
        c = new GridBagConstraints();
        JRadioButton spanish = new JRadioButton("Español");
        spanish.setActionCommand("Spanish");
        spanish.addActionListener(this);
        spanish.setEnabled(false);
        if (language == language.SPANISH)
        {
            spanish.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(spanish, c);
        
        c = new GridBagConstraints();
        JRadioButton italian = new JRadioButton("Italiano");
        italian.setActionCommand("Italian");
        italian.addActionListener(this);
        italian.setEnabled(false);
        if (language == language.ITALIAN)
        {
            italian.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(italian, c);
        
        c = new GridBagConstraints();
        JRadioButton french = new JRadioButton("Français");
        french.setActionCommand("French");
        french.addActionListener(this);
        french.setEnabled(false);
        if (language == language.FRENCH)
        {
            french.setSelected(true);
        }
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
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
                
                master.serveAction("language selected");
            }
        }
    }
}
