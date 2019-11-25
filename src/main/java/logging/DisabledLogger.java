package logging;

import print.ColorPrint;

public class DisabledLogger extends Logger{


    public void print(String level, Integer id, String message){

    }

    public void println(String level, Integer id,  String message){

    }

    public void print(String level, String message){

    }
    public void println(String level, String message){
    }

    public void setLevel(int printLevel){
    }

    public int getLevel(){
        return 0;
    }
}
