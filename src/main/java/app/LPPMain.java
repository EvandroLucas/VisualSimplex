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
            String inputFileName;
            File inputFile;
            inputFileName = "lppInput/input.txt";
            Logger.println("info", "Loading file : " + inputFileName);
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            inputFile = new File(Objects.requireNonNull(classLoader.getResource(inputFileName)).getFile());
            LPPReader lppReader = new LPPReader();
            LPP lpp = lppReader.readFromFile(inputFile);
            System.out.println(lpp);
            CanonicalLPP lpp2 = new CanonicalLPP(lpp);
            System.out.println(lpp2);
            Tableau tableau = lpp2.getTableau();
            CustomPrinter printer = new CustomPrinter();
            printer.printTableau(tableau);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
