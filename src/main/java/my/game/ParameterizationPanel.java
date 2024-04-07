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
public class ParameterizationPanel extends JPanel
{
    private int worldSize = 20;
    private int seasPercentage = 40;
    private int mountsPercentage = 40;
    private int woodsPercentage = 40;

    public ParameterizationPanel()
    {
        super(new GridBagLayout());
        
        JLabel information = new JLabel("Choose parameters of the world:");
        
        JLabel worldSizeLabel = new JLabel("World size:");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(worldSizeLabel, c);
        
        JSlider worldSizeSlider = new JSlider(JSlider.VERTICAL, 10, 30, worldSize);
        worldSizeSlider.setMajorTickSpacing(5);
        worldSizeSlider.setMinorTickSpacing(1);
        worldSizeSlider.setPaintTicks(true);
        worldSizeSlider.setPaintLabels(true);
        {
            Hashtable<Integer, JLabel> labels = new Hashtable<>(3);
            labels.put(10, new JLabel("small"));
            labels.put(20, new JLabel("normal"));
            labels.put(30, new JLabel("big"));
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
            Hashtable<Integer, JLabel> labels = new Hashtable<>(5);
            labels.put(0, new JLabel("land only"));
            labels.put(15, new JLabel("lakes"));
            labels.put(40, new JLabel("seas"));
            labels.put(65, new JLabel("oceans"));
            labels.put(80, new JLabel("islands"));
            worldSizeSlider.setLabelTable(labels);
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
            Hashtable<Integer, JLabel> labels = new Hashtable<>(5);
            labels.put(0, new JLabel("none"));
            labels.put(15, new JLabel("very few"));
            labels.put(40, new JLabel("a few"));
            labels.put(65, new JLabel("many"));
            labels.put(80, new JLabel("very many"));
            worldSizeSlider.setLabelTable(labels);
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
            Hashtable<Integer, JLabel> labels = new Hashtable<>(5);
            labels.put(0, new JLabel("none"));
            labels.put(15, new JLabel("very few"));
            labels.put(40, new JLabel("a few"));
            labels.put(65, new JLabel("many"));
            labels.put(80, new JLabel("very many"));
            worldSizeSlider.setLabelTable(labels);
        }
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        add(woodsPercentageSlider, c);
    }
}
