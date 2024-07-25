package my.world.configuration;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import my.game.Master;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldConfigurationContentPane extends JPanel
{
    private JSlider worldSizeSlider;
    private JSlider seaPercentageSlider;
    private JSlider mountainsPercentageSlider;
    
    private static final int SLIDER_WIDTH = 400;
    private static final int SLIDER_HEIGHT = 50;
    
    public WorldConfigurationContentPane(Master master)
    {
        super(new GridBagLayout());
        
        makeHeaderLabel();
        makeWorldSizeSlider();
        makeSeaPercentageSlider();
        makeMountainPercentageSlider();
        makeButtons(master);
    }
    
    private void makeHeaderLabel()
    {
        JLabel label = new JLabel("Select the World Parameters");
        label.setAlignmentX(CENTER_ALIGNMENT);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(label, c);
    }
    
    private void makeWorldSizeSlider()
    {
        JPanel worldSizeSliderPanel = new JPanel();
        worldSizeSliderPanel.setBorder(BorderFactory.createTitledBorder("World size"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(worldSizeSliderPanel, c);
        worldSizeSlider = new JSlider(JSlider.HORIZONTAL, 15, 30, 20);
        worldSizeSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        Hashtable<Integer, JLabel> worldSizeLabels = new Hashtable<>(3);
        worldSizeLabels.put(15, new JLabel("small"));
        worldSizeLabels.put(20, new JLabel("medium"));
        worldSizeLabels.put(25, new JLabel("big"));
        worldSizeLabels.put(30, new JLabel("large"));
        worldSizeSlider.setLabelTable(worldSizeLabels);
        worldSizeSlider.setPaintLabels(true);
        worldSizeSlider.setMinorTickSpacing(1);
        worldSizeSlider.setMajorTickSpacing(5);
        worldSizeSlider.setPaintTicks(true);
        worldSizeSlider.setSnapToTicks(true);
        worldSizeSliderPanel.add(worldSizeSlider);
    }
    
    private void makeSeaPercentageSlider()
    {
        JPanel seaPercentageSliderPanel = new JPanel();
        seaPercentageSliderPanel.setBorder(BorderFactory.createTitledBorder("Sea"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(seaPercentageSliderPanel, c);
        seaPercentageSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 4);
        seaPercentageSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        Hashtable<Integer, JLabel> seaPercentageLabels = new Hashtable<>(3);
        seaPercentageLabels.put( 0, new JLabel("lakes"));
        seaPercentageLabels.put( 5, new JLabel("seas"));
        seaPercentageLabels.put(10, new JLabel("oceans"));
        seaPercentageSlider.setLabelTable(seaPercentageLabels);
        seaPercentageSlider.setPaintLabels(true);
        seaPercentageSlider.setMinorTickSpacing(1);
        seaPercentageSlider.setMajorTickSpacing(5);
        seaPercentageSlider.setPaintTicks(true);
        seaPercentageSlider.setSnapToTicks(true);
        seaPercentageSliderPanel.add(seaPercentageSlider);
    }
    
    private void makeMountainPercentageSlider()
    {
        JPanel mountainsPercentageSliderPanel = new JPanel();
        mountainsPercentageSliderPanel.setBorder(BorderFactory.createTitledBorder("Mountains"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(mountainsPercentageSliderPanel, c);
        mountainsPercentageSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        mountainsPercentageSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
        Hashtable<Integer, JLabel> mountsPercentageLabels = new Hashtable<>(3);
        mountsPercentageLabels.put( 0, new JLabel("few"));
        mountsPercentageLabels.put( 5, new JLabel("medium"));
        mountsPercentageLabels.put(10, new JLabel("many"));
        mountainsPercentageSlider.setLabelTable(mountsPercentageLabels);
        mountainsPercentageSlider.setPaintLabels(true);
        mountainsPercentageSlider.setMinorTickSpacing(1);
        mountainsPercentageSlider.setMajorTickSpacing(5);
        mountainsPercentageSlider.setPaintTicks(true);
        mountainsPercentageSlider.setSnapToTicks(true);
        mountainsPercentageSliderPanel.add(mountainsPercentageSlider);
    }
    
    private void makeButtons(ActionListener master)
    {
        JButton back = new JButton("Back");
        back.setActionCommand("->players");
        back.addActionListener(master);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(back, c);
        
        JButton button = new JButton("Ready");
        button.setActionCommand("->gameplay");
        button.addActionListener(master);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(button, c);
    }

    private double getSeaPercentage()
    {
        /*  y = a * x + b,
                a = (yMax - yMin) / (xMax - xMin) = 40 / 10 = 4
                    xMin =  0
                    xMax = 10
                    yMin = 20
                    yMax = 60
                b = yMin - a * xMin = 20 - 0 = 20
        */
        return 4 * seaPercentageSlider.getValue() + 20;
    }

    private double getMountsPercentage()
    {
        /*  y = a * x + b,
                a = (yMax - yMin) / (xMax - xMin) = 30 / 10 = 3
                    xMin =  0
                    xMax = 10
                    yMin = 10
                    yMax = 40
                b = yMin - a * xMin = 10 - 0 = 10
        */
        return 3 * mountainsPercentageSlider.getValue() + 10;
    }

    public WorldConfiguration getConfiguration()
    {
        WorldConfiguration configuration = new WorldConfiguration();
        configuration.worldSide = worldSizeSlider.getValue();
        configuration.seaPercentage = 0.01 * (double) getSeaPercentage();
        configuration.mountsPercentage = 0.01 * (double) getMountsPercentage();
        return configuration;
    }
}
