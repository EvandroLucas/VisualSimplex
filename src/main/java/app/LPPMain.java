package app;

import logging.Logger;
import lpp.CanonicalLPP;
import lpp.LPP;
import lpp.LPPReader;
import print.CustomPrinter;
import simplex.Tableau;

import java.io.File;
import java.util.Objects;

public class LPPMain {

    public static void main(String[] args) {
        try {
            // Find the file
            String inputFileName;
            File inputFile;
            inputFileName = "lppInput/simpleInput.txt";
            Logger.println("info", "Loading file : " + inputFileName);
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
            // Solve, just to see...
            LPPSolver lppSolver = new LPPSolver();
            if(lppSolver.canSolve(lpp2)) {
                Logger.println("Info","Solving normal LPP");
            }
            else {
                Logger.println("Info","Solving Integer LPP");
            }
            lppSolver.solve(lpp2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
