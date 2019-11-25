package logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class LoggerProvider {

    private static HashMap<String,Logger> loggers = new HashMap<>();

    private static HashSet<String> blackList = new LinkedHashSet<>();

    private static LoggerProvider ourInstance = new LoggerProvider();

    public static LoggerProvider getInstance() {
        return ourInstance;
    }

    private LoggerProvider() {
    }

    public Logger provide(String loggerName){
        if(! loggers.containsKey(loggerName)){
            Logger logger = new DefaultLogger(loggerName);
            if(!blackList.contains(loggerName))
                loggers.put(loggerName,logger);
        }
        return loggers.get(loggerName);
    }
    public Logger provideWithParent(String loggerName, String parentName){
        if(! loggers.containsKey(loggerName)){
            Logger logger = new DefaultLogger(loggerName,parentName);
            if(!blackList.contains(loggerName))
                loggers.put(loggerName,logger);
        }
        return loggers.get(loggerName);
    }
    public void disable(String loggerName){
        blackList.add(loggerName);
        loggers.put(loggerName,new DisabledLogger());
    }

}
