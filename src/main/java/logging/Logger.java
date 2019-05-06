package logging;

import print.ColorPrint;

import java.util.HashMap;
import java.util.Map;

public class Logger {


    private String level;
    private String message;
    private static final int printLevel = 1;
    private static String newLineControl = "";


    private static Map<String, Integer> levelMap;
    static {
        levelMap = new HashMap<>();
        levelMap.put("deep",    0);
        levelMap.put("Deep",    0);
        levelMap.put("DEEP",    0);

        levelMap.put("debug",  1);
        levelMap.put("Debug",  1);
        levelMap.put("DEBUG",  1);

        levelMap.put("info",   2);
        levelMap.put("Info",   2);
        levelMap.put("INFO",   2);

        levelMap.put("warning",3);
        levelMap.put("Warning",3);
        levelMap.put("WARNING",3);

        levelMap.put("severe", 4);
        levelMap.put("Severe", 4);
        levelMap.put("SEVERE", 4);

        levelMap.put("error",  5);
        levelMap.put("Error",  5);
        levelMap.put("ERROR",  5);

        levelMap.put("off",  5);
        levelMap.put("Off",  5);
        levelMap.put("OFF",  5);

    }
    private static Map<Integer,String> printLevelMap;
    static {
        printLevelMap = new HashMap<>();
        printLevelMap.put(0," DEEP  ");
        printLevelMap.put(1," DEBUG ");
        printLevelMap.put(2," INFO  ");
        printLevelMap.put(3,"WARNING");
        printLevelMap.put(4,"SEVERE ");
        printLevelMap.put(5," ERROR ");
        printLevelMap.put(6,"  OFF  ");

    }

    private static Map<Integer,String> idColor;
    static {
        idColor = new HashMap<>();
        idColor.put(0,ColorPrint.ANSI_CYAN);
        idColor.put(1,ColorPrint.ANSI_GREEN);
        idColor.put(2,ColorPrint.ANSI_BLUE);
        idColor.put(3,ColorPrint.ANSI_PURPLE);
        for(int i=4;i< 100; i++)
            idColor.put(i,ColorPrint.ANSI_BLACK);
    }


    public static void print(String level, Integer id, String message){
        if(levelMap.get(level) >= printLevel){
            if(!newLineControl.equals(level)) {
                System.out.print(idColor.get(id) + " [" + printLevelMap.get(levelMap.get(level)) + "] : " + message + ColorPrint.ANSI_RESET);
            }
            else {
                System.out.print(message);
            }
        }
    }

    public static void println(String level, Integer id,  String message){
        print(level, id, message + "\n");
    }

    public static void print(String level, String message){
        if(levelMap.get(level) >= printLevel){
            if(!newLineControl.equals(level)) {
                System.out.print(" [" + printLevelMap.get(levelMap.get(level)) + "] : " + message);
            }
            else {
                System.out.print(message);
            }
        }
    }
    public static void println(String level, String message){
        print(level, message + "\n");
    }



}
