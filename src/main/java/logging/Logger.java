package logging;

import print.ColorPrint;

import java.util.HashMap;
import java.util.Map;

public abstract class Logger {


    protected String level;
    protected String message;
    protected String name = "---";
    protected String parent = "main";
    protected int printLevel = 1;
    protected static String newLineControl = "";

    protected static Map<String, Integer> levelMap = new HashMap<>();
    protected static Map<Integer,String> printLevelMap = new HashMap<>();
    protected static Map<Integer,String> idColor = new HashMap<>();


    protected Logger (){

        levelMap.put("deep",    0);
        levelMap.put("debug",  1);
        levelMap.put("info",   2);
        levelMap.put("warning",3);
        levelMap.put("severe", 4);
        levelMap.put("error",  5);
        levelMap.put("off",  5);

        printLevelMap.put(0," DEEP  ");
        printLevelMap.put(1," DEBUG ");
        printLevelMap.put(2," INFO  ");
        printLevelMap.put(3,"WARNING");
        printLevelMap.put(4,"SEVERE ");
        printLevelMap.put(5," ERROR ");
        printLevelMap.put(6,"  OFF  ");


        idColor.put(0,ColorPrint.ANSI_CYAN);
        idColor.put(1,ColorPrint.ANSI_GREEN);
        idColor.put(2,ColorPrint.ANSI_BLUE);
        idColor.put(3,ColorPrint.ANSI_PURPLE);
        for(int i=4;i< 100; i++) {
            idColor.put(i, ColorPrint.ANSI_BLACK);
        }
    }

    protected Logger(String loggerName){
        this();
        this.name = loggerName;
    }
    protected Logger(String loggerName, String parentLogger){
        this(loggerName);
        this.parent = parentLogger;
    }


    public abstract void print(String level, Integer id, String message);

    public abstract void println(String level, Integer id,  String message);

    public abstract void print(String level, String message);

    public abstract void println(String level, String message);

    public abstract void setLevel(int printLevel);

    public int getLevel(){
        return printLevel;
    }

}
