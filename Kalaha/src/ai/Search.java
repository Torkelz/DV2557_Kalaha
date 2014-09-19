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
    
    private class AlphaBetaMove{
        public MoveIndicator move;
        public int alpha;
        public int beta;

        public AlphaBetaMove(MoveIndicator _move, int _alpha, int _beta) {
            this.move = _move;
            this.alpha = _alpha;
            this.beta = _beta;
        }
    }
    
    
    
    
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
        return recursiveDLS(root, _problem, _maxDepth).move;
    }
    
    private AlphaBetaMove recursiveDLS(Node _currentNode, Problem _problem, int _maxDepth){
        //Save temporary state here ?!
        if(_problem.goalTest(_currentNode.getMove())){
            return new AlphaBetaMove(_currentNode.getMove(),0,0);
        }
        else if(_maxDepth == 0){
            return new AlphaBetaMove(MoveIndicator.CUTOFF,0,0);
        }
        else{
            if(_currentNode.getChildCount() == 0)
                _currentNode.populate(_problem);
            
            boolean cutoffOccured = false;
            Iterator<Node> it = _currentNode.getChildIterator();
            
            if(_problem.isMax()){
                while(it.hasNext()){                
                    AlphaBetaMove result = recursiveDLS((Node) it.next(), _problem, _maxDepth - 1);
                    if(result == MoveIndicator.CUTOFF){
                        cutoffOccured = true;
                    }
                    else if(result != MoveIndicator.FAILURE){
                        return result;
                    }
                }
            }
            else{
                while(it.hasNext()){                
                    AlphaBetaMove result = recursiveDLS((Node) it.next(), _problem, _maxDepth - 1);
                    if(result == MoveIndicator.CUTOFF){
                        cutoffOccured = true;
                    }
                    else if(result != MoveIndicator.FAILURE){
                        return result;
                    }
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
