/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Torkelz
 */
class Panel extends JPanel {
    
    private Node root = null;
    private int cRadius = 5;
    private int treeDepth = 0;
    private int verticalSpace = 5;
    private int horizontalSpace = 5;
    private final Dimension panelDimension = new Dimension(420,420);
    
    Panel() {
        // set a preferred size for the custom panel.
        setPreferredSize(panelDimension);
        
        
        Node rot = new Node(null, MoveIndicator.ONE);
        pop(rot);
        Iterator<Node> it = rot.getChildIterator();
        while(it.hasNext()){
            pop(it.next());
        }
        root = rot;
    }
    
    private void pop(Node _n){
        for(int i = 0; i < 6; i++)
            _n.addChild(MoveIndicator.ONE);
    }
        
    public void setRoot(Node _root){
        root = _root;
    }    
        
    private int getDepth(Node _root){
        if (_root == null) 
            return 0;
        else
        {
            int depth = 0;
            Iterator<Node> it = _root.getChildIterator();
            while(it.hasNext()){
                depth = Math.max(depth, getDepth(it.next()));
            }
            return ++depth;
        }
    }   
    
    private int maxDepth = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(root == null)
            return;
        
        maxDepth = getDepth(root);
        
        drawNodes(g, root, 210, 0, 0);



//        g.drawString("BLAH", 20, 20);
//        g.drawRect(200, 200, 200, 200);
    }
    
    
    private void drawNodes(Graphics g, Node _currNode, double posX, double posY, int depth){
        if(depth == 0){
            
            Iterator<Node> it = _currNode.getChildIterator();
            while(it.hasNext()){
                drawNodes(g, it.next(), posX, posY, ++depth);
            }
        }
        else{
            int nodes = (int) Math.pow(6, depth);
            double previousSpacing = panelDimension.getWidth() / (int) Math.pow(6, depth - 1);
            
            double HorizontalNodeSpace =  previousSpacing/ nodes;
            double VerticalNodeSpace =  panelDimension.getHeight() / (maxDepth + 1);
            double radius = cRadius;
//            if(cRadius > HorizontalNodeSpace * 0.5)
//                radius = HorizontalNodeSpace;
            double xx = posX / nodes;
            
            int index = 0;
            Iterator<Node> it = _currNode.getChildIterator();
            while(it.hasNext()){
                double y = posY + 50;
                double x = ((index - 3) * HorizontalNodeSpace) + posX;
                int dummy = 0;

                if(x < 0 || x > 420)
                    dummy = 2;
                if(y < 0 || y > 420)
                    dummy = 9;
                
                
                g.fillOval((int)x, (int)y, (int)radius, (int)radius);

                drawNodes(g, it.next(), x, y, ++depth);
                index++;
            }
        }
    }
}
