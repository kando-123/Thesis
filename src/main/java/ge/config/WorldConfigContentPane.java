package ge.config;

import ge.utilities.*;
import ge.world.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldConfigContentPane extends JPanel implements ActionListener
{
    private final Invoker<ConfigManager> invoker;
    
    private JSlider sizeSlider;
    private JSlider seaSlider;
    private JSlider mountainSlider;
    
    private static final int SLIDER_WIDTH = 400;
    private static final int SLIDER_HEIGHT = 50;
    
    public WorldConfigContentPane(Invoker<ConfigManager> invoker)
    {
        super(new GridBagLayout());
        
        this.invoker = invoker;
        
        makeHeaderLabel();
        makeWorldSizeSlider();
        makeSeaSlider();
        makeMountainSlider();
        makeButtons();
    }
    
    private void makeHeaderLabel()
    {
        var label = new JLabel("World Parameters");
        label.setAlignmentX(CENTER_ALIGNMENT);
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(label, c);
    }
    
    private void makeWorldSizeSlider()
    {
        var worldSizeSliderPanel = new JPanel();
        worldSizeSliderPanel.setBorder(BorderFactory.createTitledBorder("World size"));
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(worldSizeSliderPanel, c);
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 15, 30, 20);
        sizeSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        var worldSizeLabels = new Hashtable<Integer, JLabel>(3);
        worldSizeLabels.put(15, new JLabel("small"));
        worldSizeLabels.put(20, new JLabel("medium"));
        worldSizeLabels.put(25, new JLabel("big"));
        worldSizeLabels.put(30, new JLabel("large"));
        sizeSlider.setLabelTable(worldSizeLabels);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setSnapToTicks(true);
        worldSizeSliderPanel.add(sizeSlider);
    }
    
    private void makeSeaSlider()
    {
        var seaPercentageSliderPanel = new JPanel();
        seaPercentageSliderPanel.setBorder(BorderFactory.createTitledBorder("Sea"));
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(seaPercentageSliderPanel, c);
        seaSlider = new JSlider(JSlider.HORIZONTAL, 0, 8, 4);
        seaSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        var seaPercentageLabels = new Hashtable<Integer, JLabel>(3);
        seaPercentageLabels.put(0, new JLabel("lakes"));
        seaPercentageLabels.put(4, new JLabel("seas"));
        seaPercentageLabels.put(8, new JLabel("oceans"));
        seaSlider.setLabelTable(seaPercentageLabels);
        seaSlider.setPaintLabels(true);
        seaSlider.setMinorTickSpacing(1);
        seaSlider.setMajorTickSpacing(4);
        seaSlider.setPaintTicks(true);
        seaSlider.setSnapToTicks(true);
        seaPercentageSliderPanel.add(seaSlider);
    }
    
    private void makeMountainSlider()
    {
        var mountainsPercentageSliderPanel = new JPanel();
        mountainsPercentageSliderPanel.setBorder(BorderFactory.createTitledBorder("Mountains"));
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(mountainsPercentageSliderPanel, c);
        mountainSlider = new JSlider(JSlider.HORIZONTAL, 0, 8, 4);
        mountainSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        var mountsPercentageLabels = new Hashtable<Integer, JLabel>(3);
        mountsPercentageLabels.put(0, new JLabel("few"));
        mountsPercentageLabels.put(4, new JLabel("medium"));
        mountsPercentageLabels.put(8, new JLabel("many"));
        mountainSlider.setLabelTable(mountsPercentageLabels);
        mountainSlider.setPaintLabels(true);
        mountainSlider.setMinorTickSpacing(1);
        mountainSlider.setMajorTickSpacing(4);
        mountainSlider.setPaintTicks(true);
        mountainSlider.setSnapToTicks(true);
        mountainsPercentageSliderPanel.add(mountainSlider);
    }
    
    private void makeButtons()
    {
        var back = new JButton("Back ↩");
        back.setActionCommand("->players");
        back.addActionListener(this);
        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(back, c);
        
        var button = new JButton("Play ✓");
        button.setActionCommand("->gameplay");
        button.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(button, c);
    }

    private double getSeaPoints()
    {
        /*  y = a * x + b,
                a = (yMax - yMin) / (xMax - xMin) = 40 / 8 = 5
                    xMin =  0
                    xMax = 8
                    yMin = 20
                    yMax = 60
                b = yMin - a * xMin = 20 - 0 = 20
        */
        return 5 * seaSlider.getValue() + 20;
    }

    private double getMountainPoints()
    {
        /*  y = a * x + b,
                a = (yMax - yMin) / (xMax - xMin) = 30 / 8 = 3.75
                    xMin =  0
                    xMax = 8
                    yMin = 10
                    yMax = 40
                b = yMin - a * xMin = 10 - 0 = 10
        */
        return 3.75 * mountainSlider.getValue() + 10;
    }

    public WorldConfig getConfig()
    {
        int size = sizeSlider.getValue();
        double seaPercentage = 0.01 * getSeaPoints();
        double mountainsPercentage = 0.01 * getMountainPoints();
        
        return new WorldConfig(size, seaPercentage, mountainsPercentage);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "->players" ->
            {
                invoker.invoke(new BeginPlayerConfigCommand());
            }
            case "->gameplay" ->
            {
                invoker.invoke(new FinishConfigCommand());
            }
        }
    }
}