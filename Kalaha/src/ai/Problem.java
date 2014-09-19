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
    
    public Problem(GameState _initialState, int _player){
        this.currentState = _initialState.clone();
        this.initState = _initialState.clone();
        this.player = _player;
        this.otherPlayer = 1;
        if(this.player == 1)
            this.otherPlayer = 2;

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
        int value = 0;
        
        if(_current.getWinner() == player) //AI Win
            value += 50;
        else if(_current.getWinner() == 0) //Draw
            value +=10;
        else if(_current.getWinner() == otherPlayer) //AI Lose
            value -= 50;
        else{
            
        }

        return value;
    }
    
    public int getmaxPlayer(){
        return player;
    }
}