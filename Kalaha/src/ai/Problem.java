/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.ArrayList;
import java.util.List;
import kalaha.GameState;

/**
 *
 * @author Torkelz / Smurfa
 */
public class Problem {
    private GameState initState;    
    private GameState currentState;
    private int player;
    private int otherPlayer;
    
    public GameState getCurrentGS(){
        return currentState;
    }
    public GameState getInitialGS(){
        return initState;
    }
    
    public int getmaxPlayer(){
        return player;
    }
    
    public Problem(GameState _initialState, int _player){
        this.currentState = _initialState.clone();
        this.initState = _initialState.clone();
        this.player = _player;
        this.otherPlayer = 1;
        if(this.player == 1)
            this.otherPlayer = 2;

    }
    
    public Problem clone(){
        return new Problem(initState, player);
    }
    
    public void resetState(){
        currentState = initState.clone();
    }
    
//    public boolean goalTest(MoveIndicator _move){
//        if(_move.getValue() > 0 && currentState.moveIsPossible(_move.getValue())){
//            currentState.makeMove(_move.getValue());
//            return currentState.getWinner() >= 0;
//        }
//        return false;
//    }
    
    public boolean isValidMove(MoveIndicator _move){
        return currentState.moveIsPossible(_move.getValue());
    }
    
    public boolean isMax(GameState _gs){
        return player == _gs.getNextPlayer();
    }
    
    public GameState cloneGSProblem(){
        return currentState.clone();
    }
    
    public void updateCurrentGS(GameState _new){
        currentState = _new.clone();
    }
    
    public int evaluate(GameState _prev, GameState _current){
        int utility = 2;

        /// Evaluate utility based on scoring
        if(_prev.getNextPlayer() == player){
            utility += evaluateUtilityByScore(_prev, _current, player);
        }
        else{
            utility -= evaluateUtilityByScore(_prev, _current, otherPlayer) * 0.75;
        }
        
        /// Evaluate win/lose conditions
        if(_current.getWinner() == player) //AI Win
            utility += 256;
        else if(_current.getWinner() == 0) //Draw
            utility +=16;
        else if(_current.getWinner() == otherPlayer) //AI Lose
            utility -= 256;
    
        return utility;
    }
    
    public List<MoveIndicator> populate(GameState _curr){
        List<MoveIndicator> list = new ArrayList<>();
        for(MoveIndicator ind : MoveIndicator.values()){
            if(ind.getValue() > 0 && _curr.moveIsPossible(ind.getValue())){
                list.add(ind);
            }
        }
        return list;
    }
    
    private int evaluateUtilityByScore(GameState _prev, GameState _current, int _player){
        int utility = 0;
        
        int opponent = 1;
        if(_player == opponent)
            opponent = 2;
        
        
        
        int oScore = _current.getScore(opponent);
        
        int score = _current.getScore(_player);// - _prev.getScore(_player);
        if(score > 1)
            utility += score;
        else if(_current.getNextPlayer() == _prev.getNextPlayer())
            utility += 10;
        else
            utility += 2;
        if(_current.getNextPlayer() == _prev.getNextPlayer())
            utility += 10;

        utility -= oScore;
        
        
        
        
        
        
        
        
        
//        if(_player == player){
//        
            int move = -1;
            for(int i = 1; i <= 6; ++i){
                if(_prev.getSeeds(i, _player) > 0 && _current.getSeeds(i, _player) == 0 ){
                    move = i;
                    break;
                }                
            }
            if(move > 0){
                int oMove = _prev.getOppositeAmbo(move);
                if(oMove > 7)
                    oMove -= 7;

                if (_prev.getSeeds(oMove, opponent) == 0){
                    int val = _prev.getSeeds(move, _player);
                    utility += 16;//Math.pow(2, val * 0.5);;
                }
            }
//        }
        
//        int oScore = _current.getScore(opponent) - _prev.getScore(opponent);
//        if(oScore > 1)
//            utility -= Math.pow(2, oScore * 0.5);
            
        return utility; 
    }
}