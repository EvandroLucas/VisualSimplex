package branchAndBound;

import app.LPPSolver;
import lpp.CanonicalLPP;
import lpp.Restriction;
import lpp.RestrictionType;
import lpp.Solver;
import numbers.Value;
import simplex.result.Result;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class BBSolver implements Solver {

    private Queue<BBNode> queue = new PriorityBlockingQueue<>();

    public Result solve(CanonicalLPP originalLPP) {

        LPPSolver lppSolver = new LPPSolver();

        Result resultFirstNode = lppSolver.solve(originalLPP);

        for(int solutionIndex = 0; solutionIndex < resultFirstNode.getSolution().length; solutionIndex++) {

            Value value = resultFirstNode.getSolution()[solutionIndex];

            if(!value.isInteger()) {
                Value newValueGraterEquals = value.roundUp();
                Value [] leftSide =  new Value[resultFirstNode.getSolution().length];
                Arrays.fill(leftSide, 0);
                Value v = new Value();
                v.assign(1);
                leftSide[solutionIndex] = v;
                CanonicalLPP lppCopy = originalLPP;
                RestrictionType rtt = RestrictionType.GreaterOrEqualThan;
                lppCopy.restrictions.add(new Restriction(leftSide, newValueGraterEquals, rtt, "x"));


                Value newValueLessEquals = newValueGraterEquals.sub(1.0);
                CanonicalLPP lppCopy2 = originalLPP;

            }
        }

//        BBNode firstNode = new BBNode(originalLPP, resultFirstNode);
//
//        queue.add(firstNode);
//        LPPSolver lppSolver = new LPPSolver();
//        while( ! queue.isEmpty()) {
//            BBNode node = queue.remove();
//            Result result = lppSolver.solve(node.lpp);
//            if(){
//
//            }
//        }
        return null;
    }

}
