/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.game;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;


/**
 *
 * @author Kay Jay O'Nail
 */
public class ParameterizationPanel extends JPanel
{
    private JSlider worldSizeSlider; /* 10 : 30 */
    private JSlider seaPercentageSlider; /* 0.10 : 0.90 */
    private JSlider mountainsPercentageSlider; /* 0.10 : 0.50 */
    private JSlider woodsPErcentageSlider; /* 0.10 : 0.40 */
    
    public ParameterizationPanel(Dimension dimension)
    {
        setLayout(new GridBagLayout());
        
        
    }
}
