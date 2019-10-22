package lpp;

import print.ColorPrint;

public enum ProblemType {

    MAX ("MAX"),
    MIN ("MIN");

    private final String pt;

    private ProblemType(String s) {
        pt = s;
    }

    public static ProblemType fromString (String str){
        if(str.contains("max")){
            return ProblemType.MAX;
        }
        else if(str.contains("min")){
            return ProblemType.MIN;
        }
        else {
            System.out.println("Unknown problem type: " + str);
            System.exit(1);
            return ProblemType.MAX;
        }

    }
    public String toString() {
        return this.pt;
    }


}
