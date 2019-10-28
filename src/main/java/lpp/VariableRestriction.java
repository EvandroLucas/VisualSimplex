package lpp;

import numbers.Value;
import print.ColorPrint;

/*
* This class encapsulates the symbolic representation of a Variable Restriction
* For example, if we have:
*       y <= 0
*
* Our values would be:
*   group = "y"
*   right = 0
*   rtt = RestrictionType.LessOrEqualThan
*   broadRestriction = true , since y is a vector given that
*       no integer is concatenated to it (e.g y0 would be a variable of y)
* */

    // I still question myself if a VariableRestriction shouldn't have a component

public class VariableRestriction {

    private Value right;
    private RestrictionType rtt;
    private Value multiplier = new Value(1);
    private String group;
    private Integer index = 0;
    private boolean broadRestriction = true;

    // An index means is a broad restriction
    public VariableRestriction( String group, Integer index, Value right, RestrictionType rtt) {
        this(group,right,rtt);
        this.index = index;
        broadRestriction = false;
    }

    // No index means is a broad restriction
    public VariableRestriction( String group, Value right, RestrictionType rtt) {
        this.group = group;
        this.right = right;
        this.rtt = rtt;
    }

    public VariableRestriction(VariableRestriction vrt) {
        this.multiplier = new Value(vrt.multiplier);
        this.group = vrt.group;
        this.right = vrt.right;
        this.rtt = vrt.rtt;
        this.index = vrt.index;
        broadRestriction = vrt.broadRestriction;
    }

    public Value getRight() {
        return right;
    }

    public RestrictionType getRtt() {
        return rtt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
        broadRestriction = false;
    }
    public String getName() {
        if(isBroad()) return group;
        return group + index;
    }


    public Value getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(Value multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isBroad() {
        return broadRestriction;
    }

    public boolean belongsTo(VariableRestriction broadVar){
        return broadVar.group.equals(group);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(multiplier.isNotEqualTo(1)){
            stringBuilder.append(ColorPrint.blue(multiplier.toString()));
        }
        stringBuilder.append(ColorPrint.red(group));
        if(broadRestriction) {
            stringBuilder.append(ColorPrint.yellow("*"));
        }
        else {
            stringBuilder.append(ColorPrint.red(index.toString()));
        }
        stringBuilder.append(" ").append(ColorPrint.purple(rtt.toString()));
        if(!(rtt.equals(RestrictionType.MustBeBinary) || rtt.equals(RestrictionType.MustBeInteger)))
            stringBuilder.append(ColorPrint.blue(right.toString()));
        return stringBuilder.toString();
    }
}
