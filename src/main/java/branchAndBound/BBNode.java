package branchAndBound;

import lpp.CanonicalLPP;
import simplex.result.Result;

public class BBNode implements Comparable<BBNode>{

    public CanonicalLPP lpp;
    public Result result;

    public BBNode(CanonicalLPP lpp) {
        this.lpp = new CanonicalLPP(lpp);
//        this.result = new Result(result);
    }

    @Override
    public int compareTo(BBNode o) {
        return 0;
    }
}
