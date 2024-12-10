package temporary;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main extends JFrame
{
    private JEditorPane editorPane; // Editor pane to display HTML content

    public Main()
    {
        // Set up the frame
        setTitle("Help Menu Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Show Help");

        // Add action listener for help item
        helpItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showHelpWindow();
            }
        });

        helpMenu.add(helpItem);
        menuBar.add(helpMenu);

        // Set the menu bar to the frame
        setJMenuBar(menuBar);
    }

    private void showHelpWindow()
    {
        // Create help window
        JFrame helpFrame = new JFrame("Help Topics");
        helpFrame.setSize(600, 400);

        // Create tree structure for help topics
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Help Topics");
        DefaultMutableTreeNode topic1 = new DefaultMutableTreeNode("Topic 1");
        DefaultMutableTreeNode topic2 = new DefaultMutableTreeNode("Topic 2");

        root.add(topic1);
        root.add(topic2);

        JTree helpTree = new JTree(root);

        // Initialize JEditorPane for displaying HTML content
        editorPane = new JEditorPane();
        editorPane.setEditable(false); // Make it read-only
        editorPane.setContentType("text/html"); // Set content type to HTML

        JScrollPane scrollPane = new JScrollPane(editorPane);

        // Add selection listener to load HTML files on selection
        helpTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) helpTree.getLastSelectedPathComponent();
                if (selectedNode != null)
                {
                    String topicName = selectedNode.toString();
                    loadHelpContent(topicName);
                }
            }
        });

        // Layout setup
        helpFrame.setLayout(new BorderLayout());
        helpFrame.add(new JScrollPane(helpTree), BorderLayout.WEST); // Tree on the left
        helpFrame.add(scrollPane, BorderLayout.CENTER); // Editor pane on the right

        helpFrame.setVisible(true);
    }

    private void loadHelpContent(String topicName)
    {
        String resourcePath = "/help/" + topicName + ".html"; // Adjust path according to your structure

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath))
        {
            if (inputStream == null)
            {
                editorPane.setText("<html><body><h2>Help content not found.</h2></body></html>");
                return;
            }

            editorPane.read(inputStream, null); // Load HTML content

            // Optionally, you can set a default message if no content is found
            editorPane.setCaretPosition(0); // Reset caret position to top

        }
        catch (IOException e)
        {
            e.printStackTrace();
            editorPane.setText("<html><body><h2>Error loading help content.</h2></body></html>");
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            Main example = new Main();
            example.setVisible(true);
        });
    }
}
