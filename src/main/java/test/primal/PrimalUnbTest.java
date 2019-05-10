package test.primal;

import app.LPPSolver;
import logging.Logger;

import java.io.File;

public class PrimalUnbTest {

    public static void main(String[] args) {

        //String fileName = "input/dual/inputDualOtima.txt";
        String fileName = "input/input4.txt";
        //String fileName = "input/input1.txt";
        Logger.println("info","Loading file : " + fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        LPPSolver lPPSolver = new LPPSolver();
        lPPSolver.readFromFile(file);
        lPPSolver.runSimplex();

    }

}
