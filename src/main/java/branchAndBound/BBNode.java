package branchAndBound;

import lpp.CanonicalLPP;
import simplex.result.Result;

public class BBNode {

    public CanonicalLPP lpp;
    public Result result;

    public BBNode(CanonicalLPP lpp, Result result) {
        this.lpp = new CanonicalLPP(lpp);
        this.result = new Result(result);
    }
}
