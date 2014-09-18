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
}
