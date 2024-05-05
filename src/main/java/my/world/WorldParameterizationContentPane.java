package my.world;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *
 * @author Kay Jay O'Nail
 */
public class WorldParameterizationContentPane extends JPanel
{
    private final JSlider worldSizeSlider;
    private JSlider seaPercentageSlider;
    private JSlider mountsOnLandPercentageSlider;
    private JSlider woodsOnLandPercentageSlider;
    
    private static final int SLIDER_WIDTH = 200;
    private static final int SLIDER_HEIGHT = 20;
    
    public WorldParameterizationContentPane()
    {
        super(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        JLabel label = new JLabel("Select world parameters...");
        add(label, c);
        
        /* Select the world size. Range: 10-30 */
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        JPanel worldSizeSliderPanel = new JPanel();
        worldSizeSliderPanel.setBorder(BorderFactory.createTitledBorder("World size"));
        add(worldSizeSliderPanel, c);
        worldSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 30, 15);
        worldSizeSlider.setSize(SLIDER_WIDTH, SLIDER_HEIGHT);
        Hashtable<Integer, JLabel> worldSizeLabels = new Hashtable<>(4);
        worldSizeLabels.put(10, new JLabel("small"));
        worldSizeLabels.put(15, new JLabel("medium"));
        worldSizeLabels.put(20, new JLabel("big"));
        worldSizeLabels.put(30, new JLabel("large"));
        worldSizeSlider.setLabelTable(worldSizeLabels);
        worldSizeSlider.setPaintLabels(true);
        worldSizeSliderPanel.add(worldSizeSlider);
        
        /* Select seas percentage. Range: 0-80% */
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        JPanel seaPercentageSliderPanel = new JPanel();
        seaPercentageSliderPanel.setBorder(BorderFactory.createTitledBorder("Sea"));
        add(seaPercentageSliderPanel, c);
        seaPercentageSlider = new JSlider(JSlider.HORIZONTAL, 20, 70, 40);
        seaPercentageSlider.setSize(SLIDER_WIDTH, SLIDER_HEIGHT);
        Hashtable<Integer, JLabel> seaPercentageLabels = new Hashtable<>(3);
        seaPercentageLabels.put(25, new JLabel("lakes"));
        seaPercentageLabels.put(45, new JLabel("seas"));
        seaPercentageLabels.put(65, new JLabel("oceans"));
        seaPercentageSlider.setLabelTable(seaPercentageLabels);
        seaPercentageSlider.setPaintLabels(true);
        seaPercentageSliderPanel.add(seaPercentageSlider);
        
        /* Select mounts-on-land percentage. Range: 10-40% */
        
        /* Select woods-on-land percentage. Range: 5-30% */
        
    }
}
