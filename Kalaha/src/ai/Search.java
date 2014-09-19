/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.Iterator;
import kalaha.GameState;

/**
 *
 * @author Torkelz / Smurfa
 */
public class Search {
    
    private class AlphaBetaMove{
        public MoveIndicator move;
        public int alphabeta;

        public AlphaBetaMove(MoveIndicator _move, int _alphabeta) {
            this.move = _move;
            this.alphabeta = _alphabeta;
        }
    }
    
    
    
    
    public MoveIndicator DeepeningSearch(Problem _problem, int _depth){
        
        //_problem.resetState();
        
        for(int i = 0; i < _depth; ++i){
            MoveIndicator move = depthLimitedSearch(_problem, _depth);
            _problem.resetState();
            if(move != MoveIndicator.CUTOFF){
                return move;
            }
        }
        return MoveIndicator.FAILURE;
    }
    
    private MoveIndicator depthLimitedSearch(Problem _problem, int _maxDepth){
        Node root = new Node(null, MoveIndicator.FAILURE);
        //root.populate(_problem);
        return recursiveDLS(root, _problem, Integer.MIN_VALUE, Integer.MAX_VALUE, _maxDepth).move;
    }
    
    private AlphaBetaMove recursiveDLS(Node _currentNode, Problem _problem, int _alpha, int _beta, int _maxDepth){
        //Save temporary state here ?!
        GameState prevState = _problem.cloneGSProblem();
        GameState nodeState = _problem.cloneGSProblem();
        if(_currentNode.getMove().getValue() > 0)
            nodeState.makeMove(_currentNode.getMove().getValue());

        if(_maxDepth == 0 || nodeState.getWinner() > 0){
            return new AlphaBetaMove(_currentNode.getMove(), _problem.evaluate(prevState, nodeState));
        }
        
        _problem.updateCurrentGS(nodeState);
        
        if(_currentNode.getChildCount() == 0){
            _currentNode.populate(_problem);
        }
            
        Iterator<Node> it = _currentNode.getChildIterator();
        if(_problem.isMax()){
            AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, _alpha);
            while(it.hasNext()){                
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1);
                _alpha = Math.max(_alpha, result.alphabeta);
                
                if(_beta <= _alpha){
                    break;
                }
            }
            if(_currentNode.getMove().getValue() > 0){
                return new AlphaBetaMove(_currentNode.getMove(), _alpha);
            }
            else{
                return new AlphaBetaMove(result.move, _alpha);
            }
        }
        else{
            AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, _beta);
            while(it.hasNext()){                
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1);
                _beta = Math.min(_beta, result.alphabeta);
                
                if(_beta <= _alpha){
                    break;
                }
            }
            if(_currentNode.getMove().getValue() > 0){
                return new AlphaBetaMove(_currentNode.getMove(), _beta);
            }
            else{
                return new AlphaBetaMove(result.move, _beta);
            }
        }
    }
}
