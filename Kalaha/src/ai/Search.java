/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import static ai.TreeViewer.treeView;
import java.util.Iterator;
import javax.swing.JTextArea;
import kalaha.GameState;
/**
 *
 * @author Torkelz / Smurfa
 */
public class Search {
    /*
     * Maximum allowed time the AI is allowede to search for moves in each turn.
     */
    static final long MAX_TIME = 5000;
    private JTextArea text;
    
    public Search(JTextArea _text){
        text = _text;
    }
    
    public void addText(String txt)
    {
        //Don't change this
        text.append(txt + "\n");
        text.setCaretPosition(text.getDocument().getLength());
    }
    
    public class AlphaBetaMove{
        public MoveIndicator move;
        public int alphabeta;

        public AlphaBetaMove(MoveIndicator _move, int _alphabeta) {
            this.move = _move;
            this.alphabeta = _alphabeta;
        }
    }
    
    public AlphaBetaMove deepeningSearch(Problem _problem){
        long elapsedTime = 0;
        long iterationTime = 0;
        AlphaBetaMove m = new AlphaBetaMove(MoveIndicator.FAILURE, -1);
        int i;
        for(i = 1; elapsedTime + iterationTime < MAX_TIME; ++i){
            long start = System.currentTimeMillis();
            AlphaBetaMove bestMove = depthLimitedSearch(_problem.clone(), i, startTime);
            //_problem.resetState();
            if(bestMove.move != MoveIndicator.CUTOFF){
                m = bestMove;
            }
            
            long end = System.currentTimeMillis() - start;
            iterationTime = end;
            elapsedTime += end;
        }
        addText("Depth: " + i);

        return m;
    }
    
    private AlphaBetaMove depthLimitedSearch(Problem _problem, int _maxDepth, long _startTime){
        Node root = new Node(null, MoveIndicator.FAILURE);
        /*
        Clear Tree and reset the root.
        */
        if(treeView != null){
            treeView.clearTree();
            root.setTreeNode(treeView.getRoot());
        }
        return recursiveDLS(root, _problem, Integer.MIN_VALUE, Integer.MAX_VALUE, _maxDepth, _startTime);
    }
    
    private AlphaBetaMove recursiveDLS(Node _currentNode, Problem _problem, int _alpha,
            int _beta, int _maxDepth, long _startTime){
        GameState nodeState = _problem.getInitialGS();
        if(_currentNode.getMove().getValue() > 0)
            nodeState.makeMove(_currentNode.getMove().getValue());
        /*
            Expands the current node.
        */
        if(treeView != null){
            treeView.expandCurrentNode(_currentNode.getTreeNode());
        }
        
        if((System.currentTimeMillis() - _startTime) >= MAX_TIME){
            return new AlphaBetaMove(MoveIndicator.CUTOFF, _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }
        
        if(_maxDepth == 0 || nodeState.getWinner() > 0){
            return new AlphaBetaMove(_currentNode.getMove(), _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }
        
        //_problem.updateCurrentGS(nodeState);
        
        if(_currentNode.getChildCount() == 0){
            _currentNode.populate(_problem);
        }
            
        Iterator<Node> it = _currentNode.getChildIterator();
        if(_problem.isMax()){
            AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, _alpha);
            while(it.hasNext()){                
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
                _alpha = Math.max(_alpha, result.alphabeta + _alpha);
                
                if(result.move == MoveIndicator.CUTOFF)
                    return result;
                
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
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
                _beta = Math.min(_beta, result.alphabeta + _beta);
                
                if(result.move == MoveIndicator.CUTOFF)
                    return result;
                
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
