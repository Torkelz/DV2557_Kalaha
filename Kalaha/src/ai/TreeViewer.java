/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private DefaultMutableTreeNode previousNode;

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
    
    public TreeViewer(){
        super(new GridLayout(1,0));
        
        top = new DefaultMutableTreeNode("ROOT");
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
    
    //http://www.java2s.com/Code/Java/Swing-JFC/ExpandingorCollapsingAllNodesinaJTreeComponent.htm
    public void expandAll(boolean expand) {
      expandAll(tree, new TreePath(tmodel.getRoot()), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
      if (node.getChildCount() >= 0) {
        for (Enumeration e = node.children(); e.hasMoreElements();) {
          DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
          TreePath path = parent.pathByAddingChild(n);
          expandAll(tree, path, expand);
        }
      }
      if(expand)
        tree.expandPath(parent);
      else
        tree.collapsePath(parent);
    }
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    static final TreeViewer treeView = null;//new TreeViewer();
    public void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
//                UIManager.setLookAndFeel(
//                    UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeViewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(400,320));
        //Add content to the window.
        frame.add(treeView);
        
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem1, menuItem2;

        //Create the menu bar.
        menuBar = new JMenuBar();
        
        //Build the first menu.
        menu = new JMenu("Menu");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem1 = new JMenuItem("Collapse All");        
        menuItem1.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
                expandAll(false);
            }
        }); 
        menuItem2 = new JMenuItem("Expand All");        
        menuItem2.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
                expandAll(true);
            }
        }); 
        
        menu.add(menuItem1);
        menu.add(menuItem2);
        frame.setJMenuBar(menuBar);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
