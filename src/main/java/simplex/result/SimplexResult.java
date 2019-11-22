package simplex.result;

import lpp.ResultType;
import numbers.Value;
import simplex.Simplex;

/*
*
* This class aims to extract the result of a Simplex iteration.
* The idea is to be able to discard a given Simplex ou Tableau
* object if we only need the result.
*
*/

public abstract class SimplexResult {
    public Value objValue;   // Objective Value
    public Value[] solution; // Solution vector
    public Value[] certOpt;  // Certificate of Optimality
    public Value[] certUnb;  // Certificate of Unbounded
    public Value[] certInf;  // Certificate of Infeasibility
    ResultType result;       // We use it to avoid any instanceof command on child classes

    public SimplexResult(Simplex simplex){
        objValue = simplex.tableau.z;
    }

}
