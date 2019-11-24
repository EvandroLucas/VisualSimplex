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

      ;

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

        Value currentObjValue = new Value(0);
        Result result = null;

        while( ! queue.isEmpty()) {
            BBNode node = queue.remove();
            Result partialResult = lppSolver.solve(node.lpp);

            boolean isSolutionEqual = false;

            for(int i = 0; i < originalResult.getSolution().length; i++) {
                if(originalResult.getSolution()[i].isEqualTo(partialResult.getSolution()[i])){
                    isSolutionEqual = true;
                    break;
                }
            }


            if(!isSolutionEqual){

                if(node.lpp.problemType.name() == "MAX") {
                    if(partialResult.getObjValue().isSmallerEqualsThan(originalResult.getObjValue()) &&
                        partialResult.getObjValue().isGreaterEqualsThan(currentObjValue)) {

                        currentObjValue = new Value(partialResult.getObjValue());
                        result = new Result(partialResult);

                    }
                }
                // TODO Implemente MIN case

            }

        }

        return result;
    }

}
