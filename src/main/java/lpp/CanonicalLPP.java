package lpp;

/*
* Here we enforce the rules for a given Linear Programming Problem to be in the canonical form
* The standard form will be modeled withim the tableau.
*
*   All the variable values should be non-negative, e.g : x >= 0
*   All the remaining restrictions should become the <= restriction type
*   The problem type must be maximization
*
* */

import numbers.Value;

import java.util.ArrayList;

public class CanonicalLPP extends LPP{


    // This constructor must do the conversion
    public CanonicalLPP(LPP lpp){
        super(lpp);
        if (lpp.problemType.equals(ProblemType.MIN)) {
            this.objFunction = new ArrayList<>();
            for (Component cp : lpp.objFunction) {
                Component cp2 = new Component(cp);
                cp2.setMultiplier(cp2.getMultiplier().mult(-1));
                this.objFunction.add(cp2);
            }
            problemType = ProblemType.MAX;
        }

        for(int i = 0; i < restrictions.size(); i++){
            parseRestriction(restrictions.get(i));
        }
        for(int i = 0; i < varRestrictions.size(); i++){
            parseRestriction(varRestrictions.get(i));
        }
        updateRestrictions();
    }

    private void parseRestriction(Restriction rt){
        if(!rt.rtt.equals(RestrictionType.LessOrEqualThan)) {
            if (rt.rtt.equals(RestrictionType.EqualTo)) {
                rt.rtt = RestrictionType.LessOrEqualThan;
                Restriction rt2 = new Restriction(rt);
                rt2.mult(new Value(-1));
                restrictions.add(rt2);
            }
            else if (rt.rtt.equals(RestrictionType.LessThan)) {
                rt.right = new Value(rt.right.sub(0.001));
                rt.rtt = RestrictionType.LessOrEqualThan;
            }
            else {
                // In case of > or >=, we revert it
                rt.mult(new Value(-1));
                if (rt.rtt.equals(RestrictionType.GreaterThan)) {
                    rt.rtt = RestrictionType.LessThan;
                    rt.right = new Value(rt.right.sub(0.001));
                    rt.rtt = RestrictionType.LessOrEqualThan;
                }
                if (rt.rtt.equals(RestrictionType.GreaterOrEqualThan)) {
                    rt.rtt = RestrictionType.LessOrEqualThan;
                }
            }
        }
    }

    private void parseRestriction(VariableRestriction vrt){

        // Here we decompact a restriction
        if(!vrt.isBroad()){

        }

    }


}
