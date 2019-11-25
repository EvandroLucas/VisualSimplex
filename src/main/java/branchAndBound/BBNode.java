package branchAndBound;

import lpp.CanonicalLPP;
import simplex.result.Result;

public class BBNode implements Comparable<BBNode>{

    public CanonicalLPP lpp;
    public String name = "";

    public BBNode(String name, CanonicalLPP lpp) {
        this.lpp = new CanonicalLPP(lpp);
        this.name = name;
    }

    @Override
    public int compareTo(BBNode o) {
        return 0;
    }
}
