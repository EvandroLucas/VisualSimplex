import logging.Logger;
import print.ColorPrint;
import simplex.Simplex;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        ColorPrint.printYellowBack("COMEÇOU!");
        String fileName = "input/input2.txt";
        Logger.println("info","Loading file : " + fileName);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        LPPSolver lPPSolver = new LPPSolver();
        lPPSolver.readFromFile(file);
        lPPSolver.runSimplex();

    }


}
