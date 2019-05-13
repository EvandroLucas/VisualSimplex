package app;

import logging.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try {
            //input15 é problema
            String inputFileName;
            String outputFileName;

            File inputFile;
            File outputFile;

            if(args.length == 0 ) {
                inputFileName = "input/input11.txt";
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
            lPPSolver.runSimplex();

            lPPSolver.writeToFile(outputFile);

            System.out.println("Resultados: ");
            System.out.println("Otimo: " + lPPSolver.simplex.tableau.z.doubleValue());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
