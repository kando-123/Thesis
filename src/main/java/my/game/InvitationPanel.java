/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.*;
import java.util.Hashtable;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class InvitationPanel extends JPanel
{
    private int worldSize = 20;
    private int seasPercentage = 40;
    private int mountsPercentage = 40;
    private int woodsPercentage = 40;

    public InvitationPanel()
    {
        super(new GridBagLayout());
        
        JLabel information = new JLabel("Choose parameters of the world:");
        
        JLabel worldSizeLabel = new JLabel("World size:");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(worldSizeLabel, c);
        
        JSlider worldSizeSlider = new JSlider(JSlider.VERTICAL, 5, 50, worldSize);
        worldSizeSlider.setMajorTickSpacing(5);
        worldSizeSlider.setMinorTickSpacing(1);
        worldSizeSlider.setPaintTicks(true);
        worldSizeSlider.setPaintLabels(true);
        {
            Hashtable<Integer, JLabel> labels = new Hashtable<>(6);
            labels.put(5, new JLabel("tiny"));
            labels.put(10, new JLabel("small"));
            labels.put(20, new JLabel("normal"));
            labels.put(30, new JLabel("big"));
            labels.put(40, new JLabel("giant"));
            labels.put(50, new JLabel("enormous"));
            worldSizeSlider.setLabelTable(labels);
        }
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        add(worldSizeSlider, c);
        
        JLabel seasPercentageLabel = new JLabel("Seas percentage:");
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        add(seasPercentageLabel, c);
        
        JSlider seasPercentageSlider = new JSlider(JSlider.VERTICAL, 0, 80, seasPercentage);
        seasPercentageSlider.setMajorTickSpacing(5);
        seasPercentageSlider.setMinorTickSpacing(1);
        seasPercentageSlider.setPaintTicks(true);
        seasPercentageSlider.setPaintLabels(true);
        {
            Hashtable<Integer, JLabel> labels = new Hashtable<>(9);
            for (int i = 0; i < 9; ++i)
            {
                labels.put(10 * i, new JLabel(String.format("%d%%", 10*i)));
            }
            seasPercentageSlider.setLabelTable(labels);
        }
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        add(seasPercentageSlider, c);
        
        JLabel mountsPercentageLabel = new JLabel("Mounts percentage:");
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        add(mountsPercentageLabel, c);
        
        JSlider mountsPercentageSlider = new JSlider(JSlider.VERTICAL, 0, 50, mountsPercentage);
        mountsPercentageSlider.setMajorTickSpacing(5);
        mountsPercentageSlider.setMinorTickSpacing(1);
        mountsPercentageSlider.setPaintTicks(true);
        mountsPercentageSlider.setPaintLabels(true);
        {
            Hashtable<Integer, JLabel> labels = new Hashtable<>(9);
            for (int i = 0; i < 9; ++i)
            {
                labels.put(10 * i, new JLabel(String.format("%d%%", 10*i)));
            }
            mountsPercentageSlider.setLabelTable(labels);
        }
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        add(mountsPercentageSlider, c);
        
        JLabel woodsPercentageLabel = new JLabel("Woods percentage:");
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 1;
        add(woodsPercentageLabel, c);
        
        JSlider woodsPercentageSlider = new JSlider(JSlider.VERTICAL, 0, 50, woodsPercentage);
        woodsPercentageSlider.setMajorTickSpacing(5);
        woodsPercentageSlider.setMinorTickSpacing(1);
        woodsPercentageSlider.setPaintTicks(true);
        woodsPercentageSlider.setPaintLabels(true);
        {
            Hashtable<Integer, JLabel> labels = new Hashtable<>(9);
            for (int i = 0; i < 9; ++i)
            {
                labels.put(10 * i, new JLabel(String.format("%d%%", 10*i)));
            }
            woodsPercentageSlider.setLabelTable(labels);
        }
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        add(woodsPercentageSlider, c);
    }
}
