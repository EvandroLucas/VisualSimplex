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

            for(int i = 0; i < 25; i++){
                for(int j = 0; j < 3; j++){
                    System.out.printf("combinations[%d][%d] = -3; ",i,j);
                }
                System.out.println("\n");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
