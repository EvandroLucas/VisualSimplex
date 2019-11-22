package simplex.result;

import lpp.ResultType;
import simplex.Simplex;

public class InfeasibleResult extends SimplexResult {

    public InfeasibleResult(Simplex simplex){
        super(simplex);
        result = ResultType.INFEASIBLE;
        certInf = simplex.tableau.certInf.clone();
    }

}
