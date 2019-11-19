package app;

import logging.Logger;
import lpp.CanonicalLPP;
import lpp.RestrictionType;
import lpp.VariableRestriction;
import numbers.Value;
import simplex.*;


public class LPPSolver {

    public Simplex solve(CanonicalLPP lpp){

        Value[] b = new Value[lpp.restrictions.size()];
        Value[] c = new Value[lpp.objFunction.size()];

        for(int i = 0; i < b.length; i++){
            b[i] = lpp.restrictions.get(i).right;
        }
        for(int i = 0; i < c.length; i++){
            c[i] = lpp.objFunction.get(i).getMultiplier();
        }

        Simplex simplex;
        Logger.println("info","Checking simplex type");
        if (canRunDual(c,b)){
            Logger.println("info","Will run the dual simplex method");
            simplex = new DualSimplex(lpp.getTableau());
        }
        else if (canRunPrimal(c,b)){
            Logger.println("info","Will run the primal simplex method");
            simplex = new PrimalSimplex(lpp.getTableau());
        }
        else if (canRunAux()){
            Logger.println("info","Will run the auxiliar simplex method");
            simplex = new AuxSimplex(lpp.getTableau());
        }
        else {
            Logger.println("severe","Simplex denied");
            simplex = null;
            System.exit(1);
        }
        Logger.println("info","Ready to run");
        simplex.run();
        return simplex;
    }

    //public Simples addAndSolveWithRestriction(){
        // TODO : implementar
        // Devemos fazer análise de sensibilidade
    //}

    private boolean canRunDual(Value[] c, Value[] b ){
        for (Value value1 : c) {
            if (value1.isPositive()) {
                // We cannot run dual simplex:
                // a positive value was found in the objective function (c)
                return false;
            }
        }

        // If we reached this point, no positive value was found in the objective function (c)
        // Now we search for a negative value on the right side of the restrictions (b)
            // In this case, we can run the Dual Simplex.
        for (Value value : b) {
            if (value.isNegative()) {
                // We can run the dual simplex:
                // no positive value was found in the objective function (c)
                // and a negative value was found on the right side of the restrictions (b)
                return true;
            }
        }

        // If we reached this point, the AuxSimplex will be necessary,
        // since we haven't found a positive value on the objective function (c),
        // and we also haven't found a negative value on the right side of the restrictions (b).

        return false;
    }

    private boolean canRunPrimal(Value[] c, Value[] b ){

        boolean hasPositive = false;

        // We must have at least one positive value on objective function (c)
        for (Value value : c) {
            if (value.isPositive()) {
                hasPositive = true;
                break;
            }
        }
        if(!hasPositive) {
            // We cannot run primal simplex:
            // no positive value was found in the objective function (c).
            return false;
        }

        // If we reached this point, a positive value was found in the objective function (c).
        // Now we search for a negative value on the right side of the restrictions (b).
            // In this case, we'll do the Aux Simplex.
        for (Value value : b) {
            if (value.isNegative()) {
                // We cannot run the primal simplex:
                // a negative value was found on the right side of the restrictions (b).
                return false;
            }
        }

        // If we reached this point, a positive value was found in the objective function (c),
        // and no negative value was found on the right side of the restrictions (b).
        // In this case, we can do the Primal Simplex.
        return true;
    }

    private boolean canRunAux(){
        // Always doable if LPP is in canonical form
        return true;
    }

    public boolean canSolve(CanonicalLPP lpp){
        for(VariableRestriction vrt : lpp.varRestrictions){
            if(vrt.getRtt().equals(RestrictionType.MustBeInteger)){
                return false;
            }
            if(vrt.getRtt().equals(RestrictionType.MustBeBinary)){
                return false;
            }
        }
        return true;
    }


}
