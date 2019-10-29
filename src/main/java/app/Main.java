package app;

import logging.Logger;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try {
            String inputFileName;
            String outputFileName;

            File inputFile;
            File outputFile;

            if(args.length == 0 ) {
                inputFileName = "input/input.txt";
                Logger.println("info", "Loading file : " + inputFileName);
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                inputFile = new File(classLoader.getResource(inputFileName).getFile());
                outputFile = new File(Paths.get("target/out", "results.txt").toUri());
            }
            else{
                inputFileName = args[0];
                outputFileName = args[1];
                inputFile = new File(inputFileName);
                outputFile = new File(outputFileName);
            }


            LPPSolver lPPSolver = new LPPSolver();
            lPPSolver.readFromFile(inputFile);
            lPPSolver.solve();

            lPPSolver.writeToFile(outputFile);

            System.out.println("Resultados: ");
            System.out.println("Otimo: " + lPPSolver.simplex.tableau.z.doubleValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
