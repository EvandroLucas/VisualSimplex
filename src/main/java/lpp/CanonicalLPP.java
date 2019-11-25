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

import logging.Logger;
import logging.LoggerProvider;
import numbers.Value;
import simplex.Tableau;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class CanonicalLPP extends LPP implements Cloneable{

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
        update();
    }

    public CanonicalLPP(CanonicalLPP lpp){
        super(lpp);
    }


    private void update(){
        if (problemType.equals(ProblemType.MIN)) {
            for (Component cp : this.objFunction) {
                Component cp2 = new Component(cp);
                cp2.setMultiplier(cp2.getMultiplier().mult(-1));
                this.objFunction.add(cp2);
            }
            problemType = ProblemType.MAX;
        }
        for(int i = 0; i < varRestrictions.size(); i++){
            if(parseRestriction(varRestrictions.get(i))){
                varRestrictions.remove(i);
                i--;
            }
        }
        for(int i = 0; i < restrictions.size(); i++){
            parseRestriction(restrictions.get(i));
        }
        updateRestrictions();
    }

    public void addRestriction(Restriction rt){
        parseRestriction(rt);
        updateRestrictions();
    }
    public void addRestriction(VariableRestriction vrt){
        parseRestriction(vrt);
        updateRestrictions();
    }

    private void parseRestriction(Restriction rt){
        if(!rt.rtt.equals(RestrictionType.LessOrEqualThan)) { // No need to check when we already have a <= restriction
            if (rt.rtt.equals(RestrictionType.EqualTo)) { // Reverting restriction
                rt.rtt = RestrictionType.LessOrEqualThan;
                Restriction rt2 = new Restriction(rt);
                rt2.mult(new Value(-1));
                restrictions.add(rt2);
            }
            else if (rt.rtt.equals(RestrictionType.LessThan)) { // Rounding
                rt.right = new Value(rt.right.sub(0.001));
                rt.rtt = RestrictionType.LessOrEqualThan;
            }
            else {
                // In case of > or >=, we revert it
                rt.mult(new Value(-1));

                if (rt.rtt.equals(RestrictionType.GreaterThan)) { // Rounding
                    rt.rtt = RestrictionType.LessThan;
                    rt.right = new Value(rt.right.sub(0.001));
                    rt.rtt = RestrictionType.LessOrEqualThan;
                }
                if (rt.rtt.equals(RestrictionType.GreaterOrEqualThan)) { // Reverting restriction
                    rt.rtt = RestrictionType.LessOrEqualThan;
                }
            }
        }
    }
    // Returns true if restriction can be deleted
    private boolean parseRestriction(VariableRestriction vrt){

        // The only exception for the canonical would be the x >= 0 restriction
        // For now, let's assume  that every broad restriction follows this rules
        //TODO: let it be whatever it wants
        if(vrt.isBroad()){
            if((vrt.getRight().isEqualTo(0) && vrt.getRtt().equals(RestrictionType.GreaterOrEqualThan))){
                return true;
            }
            if (!(vrt.getRight().isEqualTo(0) && vrt.getGroup().equals(mainGroup) && vrt.getRtt().equals(RestrictionType.LessOrEqualThan))){
                logger.println("error","Forbidden restriction: " + vrt);
                System.exit(1);
            }
            return false;
        }
        // We also are going to leave the domain (integer, binary) variable restrictions intact
        if(vrt.getRtt().isDomain()){
            return false;
        }
        // All of the remaining variable restrictions will be converted to proper restrictions
        else{
            // We first generate an equivalent component and conflict it with the right side
            // This component will be the only element of an component-arraylist, which will be populated lately
            LinkedHashSet<Component> components = new LinkedHashSet<>();
            logger.println("info","Adding vrt : " + vrt);
            components.add( new Component(vrt.getGroup(),vrt.getIndex(),vrt.getMultiplier()));
            Restriction rt = new Restriction(components,vrt.getRight(),vrt.getRtt());
            parseRestriction(rt);
            logger.println("info","New restriction: " + rt);
            restrictions.add(rt);
            return true;
        }
    }

    // Generates a Tableau instance for this CanonicalLPP
    public Tableau getTableau(){

        int constraintNum = numberOfRestrictions();
        int variableNum = numberOfVariables();

        Value[][] A = new Value[constraintNum + 1][variableNum + 1];
        for(int i =0; i < A.length; i++){
            for(int j =0; j < A[i].length; j++){
                A[i][j] = new Value();
            }
        }

        // We let the objective value as zero
        // The objective function will be placed at the first row
        for(int i = 0; i < objFunction.size(); i++){
            A[0][i] = objFunction.get(i).getMultiplier();
        }
        // The remaining lines are all restrictions

        for (int i = 1; i <= restrictions.size(); i++) {
            Restriction rt = restrictions.get(i-1);
            int j = 0;
            for(Component component : rt.components){
                A[i][j] = component.getMultiplier();
                j++;
            }
            A[i][j] = rt.right;
        }

        return new Tableau(A,true);
    }

}
