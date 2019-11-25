package logging;

import print.ColorPrint;

public class DefaultLogger extends Logger {

    protected DefaultLogger(String loggerName){
        super(loggerName);
    }
    protected DefaultLogger(String loggerName, String parentLogger){
        super(loggerName,parentLogger);
    }

    public void print(String level, Integer id, String message){
        if(levelMap.get(level) >= printLevel){
            if(!newLineControl.equals(level)) {
                System.out.print(idColor.get(id) + " [" + printLevelMap.get(levelMap.get(level)) + "] <" + name + "> : " + message + ColorPrint.ANSI_RESET);
            }
            else {
                System.out.print(message);
            }
        }
    }

    public void println(String level, Integer id,  String message){
        print(level, id, message + "\n");
    }

    public void print(String levelParam, String message){
        String level = levelParam.toLowerCase();
        if(levelMap.get(level) >= printLevel){
            if(!newLineControl.equals(level)) {
                System.out.print(" [" + printLevelMap.get(levelMap.get(level)) + "] <" + name + "> : " + message);
            }
            else {
                System.out.print(message);
            }
        }
    }
    public void println(String level, String message){
        print(level, message + "\n");
    }

    public void setLevel(int printLevel){
        this.printLevel = printLevel;
    }

    public int getLevel(){
        return printLevel;
    }

}
