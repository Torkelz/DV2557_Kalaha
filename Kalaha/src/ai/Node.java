/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import static ai.TreeViewer.treeView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 *
 * @author Torkelz / Smurfa
 */
public class Node{
     private final MoveIndicator move;
     private Node parent;
     private List<Node> children;
     
     DefaultMutableTreeNode treeNode; // Needed for Treeviewer

    public Node(Node _parent, MoveIndicator _move){
        this.move = _move;
        this.parent = _parent;
        children = new ArrayList<>();
        
        if(treeView != null){
            treeNode = new DefaultMutableTreeNode(this);
        }
    }
    
    public void addChild(MoveIndicator _move){
        children.add(new Node(this, _move));
    }
    
    MoveIndicator getMove(){
        return move;
    }
    
    Node getParent(){
        return parent;
    }
    
    Node getNodeAt(int _index){
        return children.get(_index);
    }
    
    int getChildCount(){
        return children.size();
    }
    
    Iterator<Node> getChildIterator(){
        return children.iterator();
    }
    
    void populate(Problem _problem){
        for(MoveIndicator ind : MoveIndicator.values()){
            if(ind.getValue() > 0 && _problem.isValidMove(ind)){
                children.add(new Node(this, ind));
                if(treeView != null){
                    TreeViewer.treeView.insertChild(treeNode, children.get(children.size() - 1).getTreeNode());
                }
            }
        }
    }
    /*
     Tree viewer needed functions
    */
    public DefaultMutableTreeNode getTreeNode(){
        return treeNode;
    }
    public void setTreeNode(DefaultMutableTreeNode _node){
        treeNode = _node;
    }
    
    public String toString(){
        return Integer.toString(move.getValue());
    }
}

