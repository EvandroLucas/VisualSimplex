package lpp;

import numbers.Value;
import print.ColorPrint;

/*
* This class encapsules the symbolic representation of a Variable Restriction
* For example, if we have:
*       y <= 0
*
* Our values would be:
*   leftside = "y"
*   right = 0
*   rtt = RestrictionType.LessOrEqualThan
*   broadRestriction = true , since y is a vector given that
*       no integer is concatenated to it (e.g y0 would be a variable of y)
* */


public class VariableRestriction {

    private String leftSide;
    private Value right;
    private RestrictionType rtt;
    private boolean broadRestriction = false;

    public VariableRestriction(String leftSide, Value right, RestrictionType rtt) {
        this.leftSide = leftSide;
        this.right = right;
        this.rtt = rtt;
        if(!leftSide.matches(".*\\d.*")){
            broadRestriction = true;
        }

    }

    public String getLeftSide() {
        return leftSide;
    }

    public Value getRight() {
        return right;
    }

    public RestrictionType getRtt() {
        return rtt;
    }

    public boolean isBroad() {
        return broadRestriction;
    }

    public boolean belongsTo(VariableRestriction broadVar){
        if(broadVar.isBroad()){
            return broadVar.leftSide.contains(leftSide);
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ColorPrint.red(leftSide));
        stringBuilder.append(" ").append(ColorPrint.purple(rtt.toString()));
        if(!(rtt.equals(RestrictionType.MustBeBinary) || rtt.equals(RestrictionType.MustBeInteger)))
            stringBuilder.append(ColorPrint.blue(right.toString()));

        return stringBuilder.toString();
    }
}
