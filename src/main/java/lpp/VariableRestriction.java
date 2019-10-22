package lpp;

import numbers.Value;
import print.ColorPrint;

public class VariableRestriction {

    private String leftSide;
    private Value right;
    private RestrictionType rtt;
    private String groupId;

    public VariableRestriction(String leftSide, Value right, RestrictionType rtt, String groupId) {
        this.leftSide = leftSide;
        this.right = right;
        this.rtt = rtt;
        this.groupId = groupId;

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

    public String getGroupId() {
        return groupId;
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
