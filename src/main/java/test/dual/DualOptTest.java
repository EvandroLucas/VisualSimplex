package test.dual;

import app.LPPSolver;
import logging.Logger;

import java.io.File;

public class DualOptTest {

    public static void main(String args[]) {
        String fileName = "input/dual/inputDualOtima.txt";
        Logger.println("info", "Loading file : " + fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        LPPSolver lPPSolver = new LPPSolver();
        lPPSolver.readFromFile(file);
        lPPSolver.solve();

        Logger.println("info", "Resultado esperado: -20");

    }


}
