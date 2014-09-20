/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Torkelz / Smurfa
 */
public class Node extends Object{
    static final Visualization vz = new Visualization();
    
    
    
     private final MoveIndicator move;
     private Node parent;
     private List<Node> children;

    public Node(Node _parent, MoveIndicator _move){
        this.move = _move;
        this.parent = _parent;
        children = new ArrayList<>();
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
            if(ind.getValue() > 0 && _problem.isValidMove(ind))
                children.add(new Node(this, ind));
        }
        Iterator<Node> it = getChildIterator();
        while(it.hasNext()){
            vz.add(this, it.next());
        }
        vz.updateTree();
    }
    
    @Override
    public String toString(){
        return Integer.toString(move.getValue());
    }
    
}

