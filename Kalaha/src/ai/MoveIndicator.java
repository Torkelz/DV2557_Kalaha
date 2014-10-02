/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

/**
 *
 * @author Torkelz / Smurfa
 */
public enum MoveIndicator {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    CUTOFF(0),
    FAILURE(-1),
    TIMESUP(-2);
    private int value;
    private MoveIndicator(int _value){
        this.value = _value;
    }
    public int getValue(){
        return value;
    }
    
}
