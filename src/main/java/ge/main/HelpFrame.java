package ge.main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class HelpFrame extends JDialog
{
    private final JEditorPane editorPane;

    public HelpFrame(JFrame parent)
    {
        super(parent, "Help");
        
        class NodeDescriptor
        {
            private final String nodeName;
            private final String fileName;

            public NodeDescriptor(String nodeName, String fileName)
            {
                this.nodeName = nodeName;
                this.fileName = fileName;
            }
            
            public NodeDescriptor(String nodeName)
            {
                this(nodeName, null);
            }

            public String getFileName()
            {
                return fileName;
            }
            
            @Override
            public String toString()
            {
                return nodeName;
            }
        }
        
        var root = new DefaultMutableTreeNode(new NodeDescriptor("Help"));
        
        var configuration = new DefaultMutableTreeNode(new NodeDescriptor("Configuration"));
            var players   = new DefaultMutableTreeNode(new NodeDescriptor("Players", "config-players.html"));
            var world     = new DefaultMutableTreeNode(new NodeDescriptor("World",   "config-world.html"));
        
        var gameplay     = new DefaultMutableTreeNode(new NodeDescriptor("Gameplay"));
            var country  = new DefaultMutableTreeNode(new NodeDescriptor("Country",  "gameplay-country.html"));
            var building = new DefaultMutableTreeNode(new NodeDescriptor("Building", "gameplay-building.html"));
            var entity   = new DefaultMutableTreeNode(new NodeDescriptor("Entity",   "gameplay-entity.html"));
            var victory  = new DefaultMutableTreeNode(new NodeDescriptor("Victory",  "gameplay-victory.html"));
        
        var view          = new DefaultMutableTreeNode(new NodeDescriptor("View"));
            var shifting  = new DefaultMutableTreeNode(new NodeDescriptor("Shifting",  "view-shifting.html"));
            var zooming   = new DefaultMutableTreeNode(new NodeDescriptor("Zooming",   "view-zooming.html"));
            var resetting = new DefaultMutableTreeNode(new NodeDescriptor("Resetting", "view-resetting.html"));

        root.add(configuration);
            configuration.add(players);
            configuration.add(world);
        root.add(gameplay);
            gameplay.add(country);
            gameplay.add(building);
            gameplay.add(entity);
            gameplay.add(victory);
        root.add(view);
            view.add(shifting);
            view.add(zooming);
            view.add(resetting);

        JTree helpTree = new JTree(root);
        
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        JScrollPane scrollPane = new JScrollPane(editorPane);

        helpTree.addTreeSelectionListener((TreeSelectionEvent e) ->
        {
            var selectedNode = (DefaultMutableTreeNode) helpTree.getLastSelectedPathComponent();
            if (selectedNode != null)
            {
                var descriptor = (NodeDescriptor) selectedNode.getUserObject();
                var file = descriptor.getFileName();
                if (file != null)
                {
                    loadHelpContent(file);
                }
            }
        });
        
        setLayout(new BorderLayout());
        add(new JScrollPane(helpTree), BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void loadHelpContent(String topicName)
    {
        String resourcePath = "/help/" + topicName;
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath))
        {
            if (inputStream == null)
            {
                editorPane.setText("<html><body><h2>Help content not found.</h2></body></html>");
                return;
            }
            editorPane.read(inputStream, null);
            editorPane.setCaretPosition(0);
        }
        catch (IOException e)
        {
            editorPane.setText("<html><body><h2>Error loading help content.</h2></body></html>");
        }
    }
}
