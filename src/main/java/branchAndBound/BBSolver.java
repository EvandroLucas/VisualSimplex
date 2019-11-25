package branchAndBound;

import lpp.LPPSolver;
import logging.Logger;
import logging.LoggerProvider;
import lpp.*;
import numbers.Value;
import simplex.result.Result;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;

public class BBSolver implements Solver {

    private boolean hide = false;
    private Result bestResult = new Result();
    private Queue<BBNode> queue = new PriorityBlockingQueue<>();

    private Logger logger = LoggerProvider.getInstance().provide("BBSolver");

    public Result solve(CanonicalLPP originalLPP) {
        
        LPPSolver lppSolver = new LPPSolver();
        lppSolver.hideOutput();
        BBNode originalNode = new BBNode("0",originalLPP);
        queue.add(originalNode);
        
        while( ! queue.isEmpty()) {
            BBNode node = queue.remove();
            logger.println("INFO","Solving node: " + node.name);
            logger.print("INFO","\n"+ node.lpp.toString());
            System.out.print("Continue? : ");
            //Scanner scanner = new Scanner(System.in);
            //scanner.next();
            Result currentResult = lppSolver.solve(node.lpp);
            logger.println("INFO","" + currentResult);
            if (currentResult.getResultType().equals(ResultType.OPTIMAL)) {
                if (currentResult.isInteger()) {
                    postResult(currentResult);
                }
                else if(currentResult.getObjValue().isGreaterThan(bestResult.getObjValue())) {
                    for (int i = 0; i < currentResult.getSolution().length; i++) {
                        Value currentSolutionValue = currentResult.getSolution()[i];
                        if (currentSolutionValue.isNotInteger()) {
                            logger.println("info","Found non integer in solution["+i+"]: " + currentSolutionValue);
                            logger.println("info","Creating problem: " + node.name + ".0");
                            CanonicalLPP lppCopy = new CanonicalLPP(node.lpp);
                            RestrictionType rtt1 = RestrictionType.GreaterOrEqualThan;
                            //TODO: change this mainGroup to something else
                            VariableRestriction vrt1 = new VariableRestriction(lppCopy.mainGroup, i, new Value(currentSolutionValue.ceil()), rtt1);
                            lppCopy.addRestriction(vrt1);
                            BBNode node1 = new BBNode(node.name + ".0",lppCopy);
                            queue.add(node1);

                            logger.println("info","Creating problem: " + node.name + ".1");
                            CanonicalLPP lppCopy2 = new CanonicalLPP(node.lpp);
                            RestrictionType rtt2 = RestrictionType.LessOrEqualThan;
                            //TODO: change this mainGroup to something else
                            VariableRestriction vrt2 = new VariableRestriction(lppCopy2.mainGroup, i, new Value(currentSolutionValue.floor()), rtt2);
                            lppCopy2.addRestriction(vrt2);
                            BBNode node2 = new BBNode(node.name + ".1",lppCopy2);
                            queue.add(node2);
                            break;
                        }
                    }
                }
            }

        }

        return new Result(bestResult);
    }

    @Override
    public void hideOutput() {
        hide = true;
    }

    // Update the best result if posted is better
    private void postResult(Result postedResult){
        if(postedResult.isInteger()
                && postedResult.getObjValue().isGreaterThan(bestResult.getObjValue())
                && postedResult.getResultType().equals(ResultType.OPTIMAL)){
            logger.println("info","Best result updated: " + postedResult);
            bestResult = new Result(postedResult);
        }
    }


}