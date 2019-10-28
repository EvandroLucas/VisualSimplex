package lpp;

/*
* This class is responsible for checking redundancy between different restrictions
*
* */


import logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class LPPRestrictionParser {


    private static final RestrictionMatch[][] combinations = new RestrictionMatch[25][3];
    static {

        // <  sign
        combinations[0][0] = RestrictionMatch.REDUNDANCY;       combinations[0][1] = RestrictionMatch.REDUNDANCY;       combinations[0][2] = RestrictionMatch.ADDITION;
        combinations[1][0] = RestrictionMatch.REDUNDANCY;       combinations[1][1] = RestrictionMatch.REDUNDANCY;       combinations[1][2] = RestrictionMatch.ADDITION;
        combinations[2][0] = RestrictionMatch.CONTRADICTION;     combinations[2][1] = RestrictionMatch.CONTRADICTION;     combinations[2][2] = RestrictionMatch.ADDITION;
        combinations[3][0] = RestrictionMatch.CONTRADICTION;     combinations[3][1] = RestrictionMatch.CONTRADICTION;     combinations[3][2] = RestrictionMatch.ADDITION;
        combinations[4][0] = RestrictionMatch.CONTRADICTION;     combinations[4][1] = RestrictionMatch.CONTRADICTION;     combinations[4][2] = RestrictionMatch.ADDITION;

        // <= sign
        combinations[5][0] = RestrictionMatch.REDUNDANCY;       combinations[5][1] = RestrictionMatch.ADDITION;       combinations[5][2] = RestrictionMatch.ADDITION;
        combinations[6][0] = RestrictionMatch.REDUNDANCY;       combinations[6][1] = RestrictionMatch.REDUNDANCY;       combinations[6][2] = RestrictionMatch.ADDITION;
        combinations[7][0] = RestrictionMatch.CONTRADICTION;    combinations[7][1] = RestrictionMatch.ADDITION;       combinations[7][2] = RestrictionMatch.ADDITION;
        combinations[8][0] = RestrictionMatch.CONTRADICTION;    combinations[8][1] = RestrictionMatch.ADDITION;       combinations[8][2] = RestrictionMatch.ADDITION;
        combinations[9][0] = RestrictionMatch.CONTRADICTION;    combinations[9][1] = RestrictionMatch.CONTRADICTION;     combinations[9][2] = RestrictionMatch.ADDITION;

        // =  sign
        combinations[10][0] = RestrictionMatch.REDUNDANCY;      combinations[10][1] = RestrictionMatch.CONTRADICTION;   combinations[10][2] = RestrictionMatch.CONTRADICTION;
        combinations[11][0] = RestrictionMatch.REDUNDANCY;      combinations[11][1] = RestrictionMatch.REDUNDANCY;      combinations[11][2] = RestrictionMatch.CONTRADICTION;
        combinations[12][0] = RestrictionMatch.CONTRADICTION;   combinations[12][1] = RestrictionMatch.REDUNDANCY;      combinations[12][2] = RestrictionMatch.CONTRADICTION;
        combinations[13][0] = RestrictionMatch.CONTRADICTION;   combinations[13][1] = RestrictionMatch.REDUNDANCY;      combinations[13][2] = RestrictionMatch.REDUNDANCY;
        combinations[14][0] = RestrictionMatch.CONTRADICTION;   combinations[14][1] = RestrictionMatch.CONTRADICTION;   combinations[14][2] = RestrictionMatch.REDUNDANCY;

        // >= sign
        combinations[15][0] = RestrictionMatch.ADDITION;        combinations[15][1] = RestrictionMatch.CONTRADICTION;   combinations[15][2] = RestrictionMatch.CONTRADICTION;
        combinations[16][0] = RestrictionMatch.ADDITION;        combinations[16][1] = RestrictionMatch.ADDITION;        combinations[16][2] = RestrictionMatch.CONTRADICTION;
        combinations[17][0] = RestrictionMatch.ADDITION;        combinations[17][1] = RestrictionMatch.ADDITION;        combinations[17][2] = RestrictionMatch.CONTRADICTION;
        combinations[18][0] = RestrictionMatch.ADDITION;        combinations[18][1] = RestrictionMatch.REDUNDANCY;      combinations[18][2] = RestrictionMatch.CONTRADICTION;
        combinations[19][0] = RestrictionMatch.ADDITION;        combinations[19][1] = RestrictionMatch.ADDITION;        combinations[19][2] = RestrictionMatch.REDUNDANCY;

        // >  sign
        combinations[20][0] = RestrictionMatch.ADDITION;        combinations[20][1] = RestrictionMatch.CONTRADICTION;   combinations[20][2] = RestrictionMatch.CONTRADICTION;
        combinations[21][0] = RestrictionMatch.ADDITION;        combinations[21][1] = RestrictionMatch.CONTRADICTION;   combinations[21][2] = RestrictionMatch.CONTRADICTION;
        combinations[22][0] = RestrictionMatch.ADDITION;        combinations[22][1] = RestrictionMatch.CONTRADICTION;   combinations[22][2] = RestrictionMatch.CONTRADICTION;
        combinations[23][0] = RestrictionMatch.ADDITION;        combinations[23][1] = RestrictionMatch.REDUNDANCY;      combinations[23][2] = RestrictionMatch.REDUNDANCY;
        combinations[24][0] = RestrictionMatch.ADDITION;        combinations[24][1] = RestrictionMatch.REDUNDANCY;      combinations[24][2] = RestrictionMatch.REDUNDANCY;
    }

    private static final Map<RestrictionType,Integer> rowFirstTermMap = new HashMap();
    static {
        rowFirstTermMap.put(RestrictionType.LessThan,0);
        rowFirstTermMap.put(RestrictionType.LessOrEqualThan,5);
        rowFirstTermMap.put(RestrictionType.EqualTo,10);
        rowFirstTermMap.put(RestrictionType.GreaterOrEqualThan,15);
        rowFirstTermMap.put(RestrictionType.GreaterThan,20);
    }
    private static final Map<RestrictionType,Integer> rowSecondTermMap = new HashMap();
    static {
        rowSecondTermMap.put(RestrictionType.LessThan,0);
        rowSecondTermMap.put(RestrictionType.LessOrEqualThan,1);
        rowSecondTermMap.put(RestrictionType.EqualTo,2);
        rowSecondTermMap.put(RestrictionType.GreaterOrEqualThan,3);
        rowSecondTermMap.put(RestrictionType.GreaterThan,4);
    }


    public RestrictionMatch check (VariableRestriction rt1, VariableRestriction rt2){
        Logger.println("debug","Checking restrictions " + rt1 + " and " + rt2);
        boolean canCompare = false;
        // First we check if it even make sense to compare those two.
        //If they are the same, of course we can compare them, e.g : e.g : x1 < 0 and x1 < 1
        if(rt1.getName().equals(rt2.getName())){
            canCompare = true;
        }
        // If one belongs to another, evidently we can compare'em. e.g : x < 0 and x1 < 1
        if (rt2.belongsTo(rt1) || rt1.belongsTo(rt2)){
            canCompare = true;
        }
        // Now we must map the restriction types into indices.
        // The row is determined by the restrictions, and the column is determined by the
        // relation of the right sides of the equation

        // Let's find the row first:
        if(canCompare){
            int row = rowFirstTermMap.get(rt1.getRtt()) + rowSecondTermMap.get(rt2.getRtt());
            int column = 1; // default is equal
            //The column depends on the right side.
            if(rt1.getRight().isSmallerThan(rt2.getRight())){
                column = 0;
            }
            else if(rt1.getRight().isGreaterThan(rt2.getRight())){
                column = 2;
            }
            Logger.println("debug","Returning " + combinations[row][column]);
            return combinations[row][column];
        }
        Logger.println("debug","Returning " + RestrictionMatch.IRRELEVANT);
        return RestrictionMatch.IRRELEVANT;
    }


}
