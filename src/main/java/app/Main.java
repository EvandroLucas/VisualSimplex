package app;

import logging.Logger;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        //input15 é problema
        String fileName = "input/input15.txt";

        Logger.println("info","Loading file : " + fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        LPPSolver lPPSolver = new LPPSolver();
        lPPSolver.readFromFile(file);
        lPPSolver.runSimplex();

    }


}
