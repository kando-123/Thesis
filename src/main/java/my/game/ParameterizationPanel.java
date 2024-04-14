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
    private JSlider worldSizeSlider;
    private JSlider landPercentageSlider;
    private JSlider mountainsPercentageSlider;
    private JSlider woodsPErcentageSlider;
    
    public ParameterizationPanel(Dimension dimension)
    {
        setLayout(new GridBagLayout());
        
        
    }
}
