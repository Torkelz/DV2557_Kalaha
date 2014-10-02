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
        public boolean terminal;

        public AlphaBetaMove(MoveIndicator _move, boolean _terminal, int _alphabeta) {
            this.move = _move;
            this.terminal = _terminal;
            this.alphabeta = _alphabeta;
        }
    }
    
    public AlphaBetaMove deepeningSearch(Problem _problem){
        long elapsedTime = 0;
        long iterationTime = 0;
        AlphaBetaMove m = new AlphaBetaMove(MoveIndicator.FAILURE, false, -1);

        long startTime = System.currentTimeMillis();

        int i;
        for(i = 1; System.currentTimeMillis() - startTime < MAX_TIME; ++i){
            long start = System.currentTimeMillis();
            
            AlphaBetaMove bestMove = depthLimitedSearch(_problem.clone(), i, startTime);
            
            if(bestMove.move == MoveIndicator.TIMESUP || bestMove.terminal)
                break;
            
            if(bestMove.move.getValue() > 0){
                m = bestMove;
            }

            iterationTime = System.currentTimeMillis() - start;
            elapsedTime += iterationTime;
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
        
        if((System.currentTimeMillis() - _startTime) >= MAX_TIME){
            return new AlphaBetaMove(MoveIndicator.TIMESUP, false, 0);
        }
        
        
        GameState nodeState = _problem.getInitialGS();
        if(_currentNode.getMove().getValue() > 0)
            nodeState.makeMove(_currentNode.getMove().getValue());
        /*
            Expands the current node.
        */
        if(treeView != null){
            treeView.expandCurrentNode(_currentNode.getTreeNode());
        }
        
        
        int currentNodeEval = _problem.evaluate(_problem.getCurrentGS(), nodeState);
        if(_maxDepth == 0 ){
            return new AlphaBetaMove(_currentNode.getMove(), false, currentNodeEval);
        }
        if(nodeState.getWinner() > 0){
            return new AlphaBetaMove(_currentNode.getMove(), true, currentNodeEval);
        }
        
        
        //_problem.updateCurrentGS(nodeState);
        
        if(_currentNode.getChildCount() == 0){
            _currentNode.populate(_problem);
        }
            
        Iterator<Node> it = _currentNode.getChildIterator();
        if(_problem.isMax()){
            AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, false, _alpha);
            while(it.hasNext()){                
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
                
                
                _alpha = Math.max(_alpha, result.alphabeta);
                
                
                if(result.move == MoveIndicator.CUTOFF || result.move == MoveIndicator.TIMESUP)
                    return result;
                
                if(_beta <= _alpha){
                    break;
                }
            }
            if(_currentNode.getMove().getValue() > 0){
                return new AlphaBetaMove(_currentNode.getMove(), result.terminal, _alpha + currentNodeEval);
            }
            else{
                return new AlphaBetaMove(result.move, result.terminal, _alpha + currentNodeEval);
            }
        }
        else{
            AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, false, _beta);
            while(it.hasNext()){                
                result = recursiveDLS((Node) it.next(), new Problem(nodeState, _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
                
                _beta = Math.min(_beta, result.alphabeta);
                
                if(result.move == MoveIndicator.CUTOFF || result.move == MoveIndicator.TIMESUP)
                    return result;
                
                if(_beta <= _alpha){
                    break;
                }
            }
            if(_currentNode.getMove().getValue() > 0){
                return new AlphaBetaMove(_currentNode.getMove(), result.terminal, _beta + currentNodeEval);
            }
            else{
                return new AlphaBetaMove(result.move, result.terminal, _beta + currentNodeEval);
            }
        }
    }
}
