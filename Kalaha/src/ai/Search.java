/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.Iterator;
import java.util.List;
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
        AlphaBetaMove m = new AlphaBetaMove(MoveIndicator.FAILURE, false, -1);

        long startTime = System.currentTimeMillis();

        int i;
        for(i = 1; System.currentTimeMillis() - startTime < MAX_TIME; ++i){
            AlphaBetaMove bestMove = depthLimitedSearch(_problem, i, startTime);
            
            if(bestMove.move == MoveIndicator.TIMESUP)
                break;
            
            if(bestMove.move.getValue() > 0){
                m = bestMove;
            }
            if(bestMove.terminal)
                break;
        }
        addText("Depth: " + i);

        return m;
    }
    
    private AlphaBetaMove depthLimitedSearch(Problem _problem, int _maxDepth, long _startTime){
        AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, false, Integer.MIN_VALUE);
        List<MoveIndicator> moves = _problem.populate(_problem.getInitialGS());
            
        Iterator<MoveIndicator> it = moves.iterator();
        while(it.hasNext()){
            AlphaBetaMove value = minValue((MoveIndicator) it.next(), _problem.clone(),
                    Integer.MIN_VALUE, Integer.MAX_VALUE, _maxDepth, _startTime);
            
            if(value.alphabeta > result.alphabeta){
                result = value;
            }
            if(result.terminal)
                return result;
        }
        return result;
    }
    
    private AlphaBetaMove maxValue(MoveIndicator _currentMove, Problem _problem, int _alpha,
            int _beta, int _maxDepth, long _startTime){
        
        if((System.currentTimeMillis() - _startTime) >= MAX_TIME){
            return new AlphaBetaMove(MoveIndicator.TIMESUP, false, 0);
        }
        
        GameState nodeState = _problem.getInitialGS();
        nodeState.makeMove(_currentMove.getValue());
      
        if(nodeState.getWinner() > 0){
            return new AlphaBetaMove(_currentMove, true, 
                    _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }
        if(_maxDepth == 0 ){
            return new AlphaBetaMove(_currentMove, false, 
                    _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }        
        
        List<MoveIndicator> moves = _problem.populate(nodeState);            
        Iterator<MoveIndicator> it = moves.iterator();
        AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, false, Integer.MIN_VALUE);
            
        int value = Integer.MIN_VALUE;
        while(it.hasNext()){
            result = minValue((MoveIndicator) it.next(), new Problem(nodeState,
                _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
            
            //IF abort mission do it directly
            if(result.move == MoveIndicator.CUTOFF || result.move == MoveIndicator.TIMESUP)
                return result;
            
            value = Math.max(value, result.alphabeta);
            if(value >= _beta)
                break;

            _alpha = Math.max(_alpha, value);
        }
        return new AlphaBetaMove(_currentMove, result.terminal, value);
    }
    
    private AlphaBetaMove minValue(MoveIndicator _currentMove, Problem _problem, int _alpha,
            int _beta, int _maxDepth, long _startTime){
        
        if((System.currentTimeMillis() - _startTime) >= MAX_TIME){
            return new AlphaBetaMove(MoveIndicator.TIMESUP, false, 0);
        }
        
        GameState nodeState = _problem.getInitialGS();
        nodeState.makeMove(_currentMove.getValue());
      
        if(nodeState.getWinner() > 0){
            return new AlphaBetaMove(_currentMove, true, 
                    _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }
        if(_maxDepth == 0 ){
            return new AlphaBetaMove(_currentMove, false, 
                    _problem.evaluate(_problem.getCurrentGS(), nodeState));
        }        
        
        List<MoveIndicator> moves = _problem.populate(nodeState);            
        Iterator<MoveIndicator> it = moves.iterator();
        AlphaBetaMove result = new AlphaBetaMove(MoveIndicator.FAILURE, false, Integer.MAX_VALUE);
            
        int value = Integer.MAX_VALUE;
        while(it.hasNext()){
            result = maxValue((MoveIndicator) it.next(), new Problem(nodeState,
                _problem.getmaxPlayer()), _alpha, _beta, _maxDepth - 1, _startTime);
            
            //IF abort mission do it directly
            if(result.move == MoveIndicator.CUTOFF || result.move == MoveIndicator.TIMESUP)
                return result;
            
            value = Math.min(value, result.alphabeta);
            if(value <= _alpha)
                break;

            _beta = Math.min(_beta, value);
        }
        return new AlphaBetaMove(_currentMove, result.terminal, value);
    }
}
