package app;


import branchAndBound.BBSolverV2;
import logging.Logger;
import logging.LoggerProvider;
import lpp.CanonicalLPP;
import lpp.LPP;
import lpp.LPPReader;
import print.CustomPrinter;
import simplex.Tableau;
import simplex.result.Result;

import java.io.File;
import java.util.Objects;

public class LPPMain {

    public static void main(String[] args) {
        try {
             Logger logger = LoggerProvider.getInstance().provide("Main");
            // Find the file
            String inputFileName;
            File inputFile;
            inputFileName = "lppInput/simpleInput.txt";
            logger.println("info", "Loading file : " + inputFileName);
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            inputFile = new File(Objects.requireNonNull(classLoader.getResource(inputFileName)).getFile());
            // Read and parse the file content
            LPPReader lppReader = new LPPReader();
            LPP lpp = lppReader.readFromFile(inputFile);
            System.out.println(lpp);
            // Convert the Linear Programming Problem to a  Canonical Linear Programming Problem
            CanonicalLPP lpp2 = new CanonicalLPP(lpp);
            System.out.println(lpp2);
            // Generate a tableau
            Tableau tableau = lpp2.getTableau();
            CustomPrinter printer = new CustomPrinter();
            printer.printTableau(tableau);
            // Solve
            LPPSolver lppSolver = new LPPSolver();
            if(lppSolver.canSolve(lpp2)) {
                logger.println("Info","Solving normal LPP");
                lppSolver.solve(lpp2);
            }
            else {
                logger.println("Info","Solving Integer LPP");
                BBSolverV2 bbSolver = new BBSolverV2();
                bbSolver.hideOutput();
                Result result = bbSolver.solve(lpp2);
                System.out.println("Result: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
