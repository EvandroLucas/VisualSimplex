package lpp;

import numbers.Value;
import print.ColorPrint;

public class Restriction {

    private Value[] leftSide;
    private Value right;
    private RestrictionType rtt;
    private String id;


    public Restriction(Value[] leftSide, Value right, RestrictionType rtt, String id) {
        this.leftSide = leftSide;
        this.right = right;
        this.rtt = rtt;
        this.id = id;
    }

    public Value[] getLeftSide() {
        return leftSide;
    }

    public Value getRight() {
        return right;
    }

    public RestrictionType getRtt() {
        return rtt;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < leftSide.length; i++){
            String sign = "+";
            if(leftSide[i].isNegative() && i!=0){
                sign = "-";
            }
            if(i > 0){
                stringBuilder.append(sign);
            }
            stringBuilder.append(ColorPrint.cyan("("));
            if(leftSide[i].isNegative() && i!=0){
                stringBuilder.append(ColorPrint.blue(leftSide[i].toString().replace("-","")));
            }
            else {
                stringBuilder.append(ColorPrint.blue(leftSide[i].toString()));
            }
            stringBuilder.append(ColorPrint.cyan(")")).append(ColorPrint.red(id + i + " "));
        }
        stringBuilder.append(" ").append(ColorPrint.purple(rtt.toString())).append(" ").append(ColorPrint.blue(right.toString()));
        return stringBuilder.toString();
    }
}
