/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.Iterator;

/**
 *
 * @author Torkelz / Smurfa
 */
public class Search {    
    public MoveIndicator DeepeningSearch(Problem _problem, int _depth){
        
        _problem.resetState();
        
        for(int i = 0; i < _depth; ++i){
            MoveIndicator move = depthLimitedSearch(_problem, _depth);
            if(move != MoveIndicator.CUTOFF){
                return move;
            }
        }
        return MoveIndicator.FAILURE;
    }
    
    private MoveIndicator depthLimitedSearch(Problem _problem, int _maxDepth){
        Node root = new Node(null, MoveIndicator.FAILURE);
        //root.populate(_problem);
        return recursiveDLS(root, _problem, _maxDepth);
    }
    
    private MoveIndicator recursiveDLS(Node _currentNode, Problem _problem, int _maxDepth){
        //Save temporary state here ?!
        if(_problem.goalTest(_currentNode.getMove())){
            return _currentNode.getMove();
        }
        else if(_maxDepth == 0){
            return MoveIndicator.CUTOFF;
        }
        else{
            if(_currentNode.getChildCount() == 0)
                _currentNode.populate(_problem);
            
            boolean cutoffOccured = false;
            Iterator<Node> it = _currentNode.getChildIterator();
            
            while(it.hasNext()){
                MoveIndicator result = recursiveDLS((Node) it.next(), _problem, _maxDepth - 1);
                if(result == MoveIndicator.CUTOFF){
                    cutoffOccured = true;
                }
                else if(result != MoveIndicator.FAILURE){
                    return result;
                }
            }
            
            if(cutoffOccured){
                return MoveIndicator.CUTOFF;
            }
            else{
                return MoveIndicator.FAILURE;
            }
        }
    }
}
