package simplex.result;

import lpp.ResultType;
import simplex.Simplex;

public class UnboundedResult extends SimplexResult {

    public UnboundedResult(Simplex simplex){
        super(simplex);
        result = ResultType.UNBOUNDED;
        certUnb = simplex.tableau.certUnb.clone();
    }

}
