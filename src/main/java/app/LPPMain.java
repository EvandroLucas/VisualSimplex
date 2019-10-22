package app;

import logging.Logger;
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
            lppReader.readFromFile(inputFile);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
