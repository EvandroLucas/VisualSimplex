package app;

import logging.Logger;
import lpp.CanonicalLPP;
import lpp.LPP;
import lpp.LPPReader;

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
            LPP lpp2 = new CanonicalLPP(lpp);
            System.out.println(lpp2);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
