package branchAndBound;
import app.LPPSolver;
import lpp.CanonicalLPP;
import lpp.Solver;
import simplex.result.Result;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class BBSolver implements Solver {

    private Queue<BBNode> queue = new PriorityBlockingQueue<>();

    public Result solve(CanonicalLPP originalLPP){
        queue.add(originalLPP);
        LPPSolver lppSolver = new LPPSolver();
        while( ! queue.isEmpty()) {
            BBNode node = queue.remove();
            Result result = lppSolver.solve(node.lpp);
            if(){
                
            }
        }
    }

}
