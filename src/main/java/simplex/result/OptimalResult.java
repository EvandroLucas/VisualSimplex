package simplex.result;

import lpp.ResultType;
import simplex.Simplex;

public class OptimalResult extends SimplexResult {

    public OptimalResult(Simplex simplex){
        super(simplex);
        result = ResultType.OPTIMAL;
        certOpt = simplex.tableau.certOpt.clone();
    }

}
