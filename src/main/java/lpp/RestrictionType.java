package lpp;

import print.ColorPrint;

public enum RestrictionType {

    GreaterThan ("> "),
    LessThan ("< "),
    GreaterOrEqualThan (">="),
    LessOrEqualThan ("<="),
    EqualTo ("= "),
    MustBeInteger (": int"),
    MustBeBinary (": bin");

    private final String rtt;

    private RestrictionType(String s) {
        rtt = s;
    }

    public static RestrictionType fromString (String str){
        if(str.contains("<=")){
            return RestrictionType.LessOrEqualThan;
        }
        else if(str.contains(">=")){
            return RestrictionType.GreaterOrEqualThan;
        }
        else if(str.contains("=")){
            return RestrictionType.EqualTo;
        }
        else if(str.contains("<")){
            return RestrictionType.LessThan;
        }
        else if(str.contains(">")){
            return RestrictionType.GreaterThan;
        }
        else if(str.contains(":int")){
            return RestrictionType.MustBeInteger;
        }
        else if(str.contains(":bin")){
            return RestrictionType.MustBeBinary;
        }
        else {
            System.out.println("Unknown restriction type: " + str);
            System.exit(1);
            return RestrictionType.EqualTo;
        }

    }
    public String toString() {
        return this.rtt;
    }


}
