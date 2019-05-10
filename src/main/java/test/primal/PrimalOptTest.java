package test.primal;

import logging.Logger;

import java.io.File;
import app.LPPSolver;

public class PrimalOptTest {

    public static void main(String[] args) {

        //String fileName = "input/dual/inputDualOtima.txt";
        String fileName = "input/primal/opt/input1.txt";
        //String fileName = "input/input1.txt";
        Logger.println("info","Loading file : " + fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        LPPSolver lPPSolver = new LPPSolver();
        lPPSolver.readFromFile(file);
        lPPSolver.runSimplex();

        System.out.println("O resultado esperado é 14");

    }

}
