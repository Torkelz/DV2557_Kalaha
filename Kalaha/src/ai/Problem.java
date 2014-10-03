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
    private int ai;
    private int opponent;
    
    public GameState getCurrentGS(){
        return currentState;
    }
    public GameState getInitialGS(){
        return initState;
    }
    
    public int getmaxPlayer(){
        return ai;
    }
    
    public Problem(GameState _initialState, int _player){
        this.currentState = _initialState.clone();
        this.initState = _initialState.clone();
        this.ai = _player;
        this.opponent = 1;
        if(this.ai == 1){
            this.opponent = 2;
        }
    }
    
    public Problem clone(){
        return new Problem(initState, ai);
    }
    
    public void resetState(){
        currentState = initState.clone();
    }
    
    public boolean isValidMove(MoveIndicator _move){
        return currentState.moveIsPossible(_move.getValue());
    }
    
    public boolean isMax(GameState _gs){
        return ai == _gs.getNextPlayer();
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
        if(_prev.getNextPlayer() == ai){
            utility += evaluateUtilityByScore(_prev, _current, ai);
        }
        else{
            utility -= evaluateUtilityByScore(_prev, _current, opponent) * 0.75;
        }
        
        /// Evaluate win/lose conditions
        if(_current.getWinner() == ai){ //AI Win
            utility += 256;
        }
        else if(_current.getWinner() == 0){ //Draw
            utility +=16;
        }
        else if(_current.getWinner() == opponent){ //AI Lose
            utility -= 256;
        }
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
        
        /// Check to find the opponent
        int tOpponent = 1;
        if(_player == tOpponent){
            tOpponent = 2;
        }
        /// Try not to give opponent score
        utility -= _current.getScore(tOpponent);

        /// Utility based on score
        int score = _current.getScore(_player);
        if(score > 1){
            utility += score;
        }
        else{
            utility += 2;
        }

        /// Check for extra move
        if(_current.getNextPlayer() == _prev.getNextPlayer()){
            utility += 4;
        }
    
        /// Try to figure out a move to prevent the opponent from stealing score
        int move = -1;
        for(int i = 1; i <= 6; ++i){
            if(_prev.getSeeds(i, _player) > 0 && _current.getSeeds(i, _player) == 0 ){
                move = i;
                break;
            }                
        }
        if(move > 0){
            int oMove = _prev.getOppositeAmbo(move);
            if(oMove > 7){
                oMove -= 7;
            }
            if (_prev.getSeeds(oMove, tOpponent) == 0){
                int val = _prev.getSeeds(move, _player);
                utility += val;//Math.pow(2, val * 0.5);;
            }
        }
        return utility; 
    }
}