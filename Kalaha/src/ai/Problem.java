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
    private GameState initialState;
    private GameState currentState;
    private int player;
    
    public Problem(GameState _initialState, int _player){
        this.initialState = _initialState.clone();
        this.currentState = _initialState.clone();
        this.player = _player;
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
    
    public void resetState(){
        currentState = initialState.clone();
    }
    public boolean isMax(){
        return player == currentState.getNextPlayer();
    }
}