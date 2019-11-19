package branchAndBound;

import app.LPPSolver;
import lpp.CanonicalLPP;
import numbers.Value;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class BBSolver {

    private Integer bestResult = 0;

    public BBSolver(CanonicalLPP lpp){
        queue.add(lpp);
    }

    public void solve(){
        CanonicalLPP lpp = queue.remove();
        LPPSolver lppSolver = new LPPSolver();
        lppSolver.solve(lpp);
    }

}
