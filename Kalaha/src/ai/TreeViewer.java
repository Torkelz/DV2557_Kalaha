/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Torkelz
 */
public class TreeViewer  extends JPanel{
    private JTree tree;
    private DefaultMutableTreeNode top;
    private DefaultTreeModel tmodel;
    
    private static boolean DEBUG = false;
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    private DefaultMutableTreeNode previousNode;
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
    
    public TreeViewer(){
        super(new GridLayout(1,0));
        
        top = new DefaultMutableTreeNode("The Java Series");
        //createNodes(top);
        previousNode = top;
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tmodel = (DefaultTreeModel)tree.getModel();

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }
        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        
        //Add the split pane to this panel.
        add(treeView);
    }
    public void insertChild(DefaultMutableTreeNode _parent, DefaultMutableTreeNode _child){
        tmodel.insertNodeInto(_child, _parent, _parent.getChildCount()); 
    }
    
    public DefaultMutableTreeNode getRoot(){
        return top;
    }
    
    public void clearTree(){
        top.removeAllChildren();
        tmodel.reload();
    }
    
    public void expandCurrentNode(DefaultMutableTreeNode _node){
        if(previousNode != _node.getParent())
            tree.collapsePath(new TreePath(previousNode.getPath()));
        tree.expandPath(new TreePath(_node.getPath()));
        
        previousNode = _node;
    }
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    static final TreeViewer treeView = new TreeViewer();
    public static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(400,320));
        //Add content to the window.
        frame.add(treeView);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
