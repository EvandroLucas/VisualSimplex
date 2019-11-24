package simplex.result;

import lpp.ResultType;
import numbers.Value;
import simplex.Simplex;

/*
*
* This class aims to extract the resultType of a Simplex iteration.
* The idea is to be able to discard a given Simplex ou Tableau
* object if we only need the resultType.
*
* Results must be read-only
*
*/


public class Result {
    private Value objValue;   // Objective Value
    private Value[] solution; // Solution vector
    private Value[] certOpt;  // Certificate of Optimality
    private Value[] certUnb;  // Certificate of Unbounded
    private Value[] certInf;  // Certificate of Infeasibility
    private ResultType resultType;       // We use it to avoid any instanceof command on child classes

    public Result(Simplex simplex){
        objValue = simplex.tableau.z;
        solution = simplex.tableau.solution;
    }
    public Result(Result result){
        this.objValue = new Value(result.objValue);
        this.solution = result.solution.clone();
        this.certOpt = result.certOpt.clone();
        this.certUnb = result.certUnb.clone();
        this.certInf = result.certInf.clone();
        this.resultType = result.resultType;
    }

    public boolean isInteger(){
        if(resultType.equals(ResultType.OPTIMAL)){
            for(Value v : solution){
                if(! v.isInteger()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Value getObjValue() {
        return objValue;
    }

    public Value[] getSolution() {
        return solution;
    }
}
