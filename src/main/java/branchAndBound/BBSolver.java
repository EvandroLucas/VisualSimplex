package branchAndBound;

import app.LPPSolver;
import lpp.CanonicalLPP;
import lpp.Restriction;
import lpp.RestrictionType;
import lpp.Solver;
import numbers.Value;
import simplex.result.Result;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class BBSolver implements Solver {

    private Queue<BBNode> queue = new PriorityBlockingQueue<>();

    public Result solve(CanonicalLPP originalLPP) {

        LPPSolver lppSolver = new LPPSolver();


        Result originalResult = lppSolver.solve(originalLPP);

        for(int solutionIndex = 0; solutionIndex < originalResult.getSolution().length; solutionIndex++) {

            Value value = originalResult.getSolution()[solutionIndex];

            if(!value.isInteger()) {
                Value newValueGraterEquals = value.roundUp();

                Value [] leftSide =  new Value[originalResult.getSolution().length];

                for(int i = 0; i < leftSide.length; i++) {
                    if(i == solutionIndex)
                        leftSide[i] = new Value(1);
                    else
                        leftSide[i] = new Value(0);
                }

                CanonicalLPP lppCopy = new CanonicalLPP(originalLPP);
                RestrictionType rtt = RestrictionType.GreaterOrEqualThan;
                lppCopy.restrictions.add(new Restriction(leftSide, newValueGraterEquals, rtt, "x"));
                BBNode firstNode = new BBNode(lppCopy);
                queue.add(firstNode);

                CanonicalLPP lppCopy2 = new CanonicalLPP(originalLPP);
                rtt = RestrictionType.LessOrEqualThan;
                Value newValueLessEquals = newValueGraterEquals.sub(1.0);
                lppCopy2.restrictions.add(new Restriction(leftSide, newValueLessEquals, rtt, "x"));
                BBNode secondNode = new BBNode(lppCopy2);
                queue.add(secondNode);

            }
        }


        while( ! queue.isEmpty()) {
            BBNode node = queue.remove();
            Result result = lppSolver.solve(node.lpp);
        }
        return null;
    }

}
