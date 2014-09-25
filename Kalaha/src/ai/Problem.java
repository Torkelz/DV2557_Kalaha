/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

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
    
    public boolean goalTest(MoveIndicator _move){
        if(_move.getValue() > 0 && currentState.moveIsPossible(_move.getValue())){
            currentState.makeMove(_move.getValue());
            return currentState.getWinner() >= 0;
        }
        return false;
    }
    
    public boolean isValidMove(MoveIndicator _move){
        return currentState.moveIsPossible(_move.getValue());
    }
    
    public boolean isMax(){
        return player == currentState.getNextPlayer();
    }
    
    public GameState cloneGSProblem(){
        return currentState.clone();
    }
    
    public void updateCurrentGS(GameState _new){
        currentState = _new.clone();
    }
    
    public int evaluate(GameState _prev, GameState _current){
        int utility = 0;

        /// Evaluate score utility of scoring
        if(_prev.getNextPlayer() == player){
            int score = _current.getScore(player) - _prev.getScore(player);
            if(score > 1)
                utility += score;
            else if(_current.getNextPlayer() == _prev.getNextPlayer())
                utility +=5;
            else
                utility += 2;
        }
        else{
            int score = _current.getScore(otherPlayer) - _prev.getScore(otherPlayer);
            if(score > 1)
                utility -= score;
            else if(_current.getNextPlayer() == _prev.getNextPlayer())
                utility -=5;
            else
                utility -= 1;
        }
        
        
        /// Evaluate win/lose conditions
        if(_current.getWinner() == player) //AI Win
            utility += 50;
        else if(_current.getWinner() == 0) //Draw
            utility +=10;
        else if(_current.getWinner() == otherPlayer) //AI Lose
            utility -= 50;
        else{
            
        }

        return utility;
    }
}